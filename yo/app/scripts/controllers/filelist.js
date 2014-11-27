'use strict';
/* jshint ignore:start */
/**
 * @ngdoc function
 * @name homearchiveApp.controller:FilelistCtrl
 * @description # FilelistCtrl Controller of the homearchiveApp
 */
angular
  .module('homearchiveApp')
  .controller(
  'FileListCtrl',
  [
    '$rootScope',
    '$scope',
    '$location',
    '$filter',
    'FileService',
    'ngTableParams',
    function ($rootScope, $scope, $location, $filter, FileService, ngTableParams) {
      if (!$rootScope.isAuth()) {
        $location.path('/login');
      }
      ;

      $scope.files = [];
      $scope.file = {};
      $scope.file.metadata = {};

      var scopeFile;
      var clean;

      $scope.tableParams = new ngTableParams(
        {
          page: 1, // show first page
          count: 10,
          // count per page
          sorting: {
            docDate: "desc"// initial sorting
          }
        },
        {
          total: $scope.files.length, // length
          // of
          // data
          getData: function ($defer, params) {

            var filteredData = params.filter() ? $filter(
              'filter')($scope.files,
              params.filter())
              : $scope.files;

            var orderedData = params.sorting() ? $filter(
              'orderBy')(filteredData,
              params.orderBy())
              : $scope.files;

            params.total(orderedData.length);
            $defer.resolve(orderedData.slice(
              (params.page() - 1)
              * params.count(),
              params.page()
              * params.count()));

          }
        });
      $scope.tableParams.settings().$scope = $scope;

      // does search with parameters
      $scope.submit = function () {
        //submit only in case of search, no edit
        if ($scope.editStatus !== true) {
          var queryParams = {
              'params': {
                fileName: $scope.file.fileName,
                startDate: $scope.file.startDate,
                endDate: $scope.file.endDate,
                metadata: {
                  description: $scope.file.metadata.description
                }
              }
            }
            ;

          FileService.query(queryParams).$promise
            .then(function (data) {
              $scope.files = data;
              console.log(JSON
                .stringify($scope.files));

              $scope.tableParams.reload();
            });
        }
      };

      // clears the form
      clean = function () {
        $scope.file.fileName = null;
        $scope.file.metadata = {};
        $scope.file.startDate = null;
        $scope.file.endDate = null;
      };
      $scope.clear = function () {
        clean();
      }

      // deletes file
      $scope.remove = function (file) {
        if (confirm('Are you sure you want to delete '
          + file.filename + ' from the database?') == false) {
          return;
        } else {
          console.log('Removing file with id: '
          + file._id);
          FileService.remove({
            id: file._id
          }).$promise.then(function () {
              var idx = $scope.files.indexOf(file);
              $scope.files.splice(idx, 1);
              console.log('removed from scope, idx: '
              + $scope.files.indexOf(file));
              $scope.tableParams.reload();
            });
        }
        ;
      };

      // pushes values into the model for editing
      $scope.edit = function (file) {
        $scope.editStatus = true;
        $scope.file.fileName = file.filename;
        $scope.file.metadata.description = file.metadata.description;
        scopeFile = file;

      };

      // submits updated values to the service
      $scope.update = function () {
        FileService.update({
            id: scopeFile._id
          }, {
            filename: $scope.file.fileName,
            metadata: {
              description: $scope.file.metadata.description
            }
          }
        ).$promise.then(function () {
            $scope.editStatus = false;
            alert('File is updated');
            clean();
          }
        );
      };

    }]);
/* jshint ignore:end */

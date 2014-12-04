'use strict';
/* jshint ignore:start */
/**
 * @ngdoc function
 * @name homearchiveApp.controller:UploadCtrl
 * @description # UploadCtrl Controller of the homearchiveApp
 */
angular.module('homearchiveApp').controller('UploadCtrl',
  ['$rootScope','$scope','$location', 'FileUploader', function ($rootScope, $scope, $location, FileUploader) {
	  if (!$rootScope.isAuth()) {
  		$location.path('/login');
  	};


    $scope.uploader = new FileUploader({
      url: 'api/fileService',
      headers:{
    	  'X-Auth-Token': $rootScope.authToken
      }
    });

    $scope.uploader.onBeforeUploadItem = function (item) {
      var data = [{
        description: $scope.description
      }]
      console.log("File description :" + angular.toJson(data));
      Array.prototype.push.apply(item.formData, data);
    };
  }]);

/* jshint ignore:end */

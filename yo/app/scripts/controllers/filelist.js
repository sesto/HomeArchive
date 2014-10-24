'use strict';

/**
 * @ngdoc function
 * @name homearchiveApp.controller:FilelistCtrl
 * @description # FilelistCtrl Controller of the homearchiveApp
 */
angular.module('homearchiveApp').controller(
		'FileListCtrl',
		[
				'$scope',
				'FileService',
				'ngTableParams',
				function($scope, FileService, ngTableParams) {
					$scope.files = [];
					var scopeFile;
					var clear;
					$scope.tableParams = new ngTableParams({
						page : 1, // show first page
						count : 10
					// count per page
					}, {
						total : $scope.files.length, // length of data
						getData : function($defer, params) {
							params.total($scope.files.length);
							$defer.resolve($scope.files.slice(
									(params.page() - 1) * params.count(),
									params.page() * params.count()));
						}

					});

					$scope.submit = function() {

						FileService.query({

							fileName : $scope.fileName,
							startDate : $scope.startDate,
							endDate : $scope.endDate,
							tag1 : $scope.tag1,
							tag2 : $scope.tag2,
							tag3 : $scope.tag3

						}).$promise.then(

						function(data) {
							$scope.files = data;
							console.log(JSON.stringify($scope.files));
							$scope.getTag = function(file) {
								return angular.isArray(file.tags) ? file.tags
										: [ file.tags ];
							}

							$scope.tableParams.reload();
						});
					};

					clear = function() {
						$scope.fileName = null;
						$scope.tag1 = null;
						$scope.tag2 = null;
						$scope.tag3 = null;
						$scope.startDate = null;
						$scope.endDate = null;
					}

					$scope.remove = function(file) {
						console.log("Removing file with id: " + file.id);
						FileService.remove({
							id : file.id
						}).$promise.then(function() {
							var idx = $scope.files.indexOf(file);
							$scope.files.splice(idx, 1);
							console.log("removed from scope, idx: "
									+ $scope.files.indexOf(file));
							$scope.tableParams.reload();
						})
					};

					$scope.edit = function(file) {
						$scope.editStatus = true;
						$scope.fileName = file.fileName;
						$scope.tag1 = file.tags[0];
						$scope.tag2 = file.tags[1];
						$scope.tag3 = file.tags[2];
						scopeFile = file;

					};

					$scope.update = function() {
						FileService.update({
							id : scopeFile.id
						}, {
							fileName : $scope.fileName,
							tags : [ $scope.tag1, $scope.tag2, $scope.tag3,

							]
						}

						).$promise.then(function() {
							$scope.editStatus = false;
							alert('File is updated');
							clear();
						}

						);
					}

				} ]);

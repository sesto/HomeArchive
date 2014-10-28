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
					$scope.file = {};
					$scope.file.tags = [];

					$scope.addTag = function() {
						$scope.file.tags.push($scope.tag);
						$scope.tag = '';
						$scope.removeTag = function(index) {
							$scope.file.tags.splice(index, 1);

						}
					};
					var scopeFile;
					var clean;
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

					// does search with parameters
					$scope.submit = function() {
						console.log($scope.file.tags);
						var queryParams = {
							'params' : {
								fileName : $scope.file.fileName,
								startDate : $scope.file.startDate,
								endDate : $scope.file.endDate,
								tags : $scope.file.tags
							}
						};

						FileService.query(queryParams).$promise.then(function(
								data) {
							$scope.files = data;
							console.log(JSON.stringify($scope.files));
							$scope.getTag = function(file) {
								return angular.isArray(file.tags) ? file.tags
										: [ file.tags ];
							};

							$scope.tableParams.reload();
						});
					};

					// clears the form
					clean = function() {
						$scope.file.fileName = null;
						$scope.file.tags = [];
						$scope.file.startDate = null;
						$scope.file.endDate = null;
					};
					$scope.clear = function() {
						clean();
					}

					// deletes file
					$scope.remove = function(file) {
						if (confirm("Are you sure you want to delete "
								+ file.fileName + ' from the database?') == false) {
							return;
						} else {
							console.log('Removing file with id: ' + file.id);
							FileService.remove({
								id : file.id
							}).$promise.then(function() {
								var idx = $scope.files.indexOf(file);
								$scope.files.splice(idx, 1);
								console.log('removed from scope, idx: '
										+ $scope.files.indexOf(file));
								$scope.tableParams.reload();
							});
						};
					};

					// pushes values into the model for editing
					$scope.edit = function(file) {
						$scope.editStatus = true;
						$scope.file.fileName = file.fileName;
						$scope.file.tags = file.tags;
						scopeFile = file;

					};

					// submits updated values to the service
					$scope.update = function() {
						FileService.update({
							id : scopeFile.id
						}, {
							fileName : $scope.file.fileName,
							tags : $scope.file.tags
						}

						).$promise.then(function() {
							$scope.editStatus = false;
							alert('File is updated');
							clean();
						}

						);
					};

				} ]);

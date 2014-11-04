'use strict';

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
						'$scope',
						'$filter',
						'FileService',
						'ngTableParams',
						function($scope, $filter, FileService, ngTableParams) {
							$scope.files = [];
							$scope.file = {};
							$scope.file.metadata = {};
							var data;

							var scopeFile;
							var clean;
							$scope.tableParams = new ngTableParams(
									{
										page : 1, // show first page
										count : 10,
										// count per page
										sorting : {
											docDate : "desc"// initial sorting
										}
									},
									{
										total : $scope.files.length, // length
										// of
										// data
										getData : function($defer, params) {

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

							// does search with parameters
							$scope.submit = function() {
							
								var queryParams = {
									'params' : {
										fileName : $scope.file.fileName,
										startDate : $scope.file.startDate,
										endDate : $scope.file.endDate,
										metadata : {
											description : $scope.file.metadata.description
										}
									}
								};

								FileService.query(queryParams).$promise
										.then(function(data) {
											$scope.files = data;
											console.log(JSON
													.stringify($scope.files));
											$scope.getTag = function(file) {
												return angular
														.isArray(file.tags) ? file.tags
														: [ file.tags ];
											};

											$scope.tableParams.reload();
										});
							};

							// clears the form
							clean = function() {
								$scope.file.fileName = null;
								$scope.file.metadata = {};
								$scope.file.startDate = null;
								$scope.file.endDate = null;
							};
							$scope.clear = function() {
								clean();
							}

							// deletes file
							$scope.remove = function(file) {
								if (confirm("Are you sure you want to delete "
										+ file.filename + ' from the database?') == false) {
									return;
								} else {
									console.log('Removing file with id: '
											+ file._id);
									FileService.remove({
										id : file._id
									}).$promise.then(function() {
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
							$scope.edit = function(file) {
								$scope.editStatus = true;
								$scope.file.fileName = file.filename;
								$scope.file.metadata.description = file.metadata.description;
								scopeFile = file;

							};

							// submits updated values to the service
							$scope.update = function() {
								FileService.update({
									id : scopeFile._id
								}, {
									filename : $scope.file.fileName,
									metadata:{
										description: $scope.file.metadata.description
									}
								}

								).$promise.then(function() {
									$scope.editStatus = false;
									alert('File is updated');
									clean();
								}

								);
							};

						} ]);

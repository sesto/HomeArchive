'use strict';

/**
 * @ngdoc function
 * @name homearchiveApp.controller:UploadCtrl
 * @description # UploadCtrl Controller of the homearchiveApp
 */
angular
		.module('homearchiveApp')
		.controller(
				'UploadCtrl',
				[
						'$scope',
						'$upload',
						function($scope, $upload) {
							$scope.tags = [];
							$scope.addTag = function() {

								$scope.tags.push($scope.tag);
								$scope.tag = '';
								$scope.removeTag = function(index) {
									$scope.tags.splice(index, 1);
								};
							};

							$scope.onFileSelect = function($files) {

								var resetMessage = function() {
									$scope.successMessage = null;
									$scope.errorMessage = null;
									$scope.file = null;
								};
								resetMessage();

								for (var i = 0; i < $files.length; i++) {
									var file = $files[i];
									$scope.file = file.name;
									$scope.upload = $upload
											.upload({
												url : 'rs/findFiles',
												method : 'POST',

												data : {
													tags : $scope.tags
												},
												file : file,
											})
											.progress(
													function(evt) {
														console
																.log('percent: '
																		+ parseInt(100.0
																				* evt.loaded
																				/ evt.total));

														$scope.progress = function() {
															var percent = parseInt(100.0
																	* evt.loaded
																	/ evt.total);
															$scope.dynamic = percent;
														};
														$scope.progress();

													})
											.success(
													function(data, status,
															headers, config) {
														console.log(status);
														$scope.successMessage = 'File uploaded successfully!';
														$scope.dynamic = 0;
														var resetTags = function() {
															$scope.tags = [];
														}
														resetTags();
													})
											.error(
													function(status) {
														$scope.errorMessage = 'Problem uploading file. Error '
																+ status + '';

													});

								}

							};

						} ]);

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
				['$scope',
						'$upload',
						function($scope, $upload) {
					 $scope.tags = [];
					$scope.addTag = function () {
						
						  $scope.tags.push($scope.tag);
						  $scope.tag = '';
						   $scope.removeTag = function (index) {
						      $scope.tags.splice(index, 1);
						    };
						};
				
							$scope.onFileSelect = function($files) {
								// $files: an array of files selected, each
								// file
								// has name, size,
								// and type.
								var reset = function(){
									$scope.successMessage = null;
									}
									reset();
								for (var i = 0; i < $files.length; i++) {
									var file = $files[i];
									$scope.file = file.name;
									$scope.upload = $upload
											.upload({
												url : 'rs/findFiles', // upload.php
												// script, node.js
												// route, or servlet
												// url
												method : 'POST',
												// headers:
												// {'header-key':
												// 'header-value'},
												// withCredentials:
												// true,
												data : {
													tags : $scope.tags
												},
												file : file, // or
											// list
											// of
											// files
											// ($files)
											// for
											// html5
											// only
											// fileName: 'doc.jpg'
											// or
											// ['1.jpg', '2.jpg',
											// ...]
											// // to
											// modify the name of
											// the
											// file(s)
											// customize file
											// formData
											// name
											// ('Content-Disposition'),
											// server side file
											// variable
											// name.
											// fileFormDataName:
											// myFile,
											// //or a list of names
											// for
											// multiple files
											// (html5).
											// Default is 'file'
											// customize how data is
											// added to formData.
											// See
											// #40#issuecomment-28612000
											// for sample code
											// formDataAppender:
											// function(formData,
											// key,
											// val){}
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
														}
														$scope.progress();
													
													})
											.success(
													function(data, status,
															headers, config) {
														console.log(status);

														$scope.successMessage = "File uploaded successfully!";
														$scope.dynamic = 0;
													})
											.error(
													function(status) {
														$scope.errorMessage = "Problem uploading file. Error "
																+ status + "";

													});
									// .then(success, error, progress);
									// access or attach event listeners to
									// the
									// underlying
									// XMLHttpRequest.
									// .xhr(function(xhr){xhr.upload.addEventListener(...)})
								}
								/*
								 * alternative way of uploading, send the file
								 * binary with the file's content-type. Could be
								 * used to upload files to CouchDB, imgur,
								 * etc... html5 FileReader is needed. It could
								 * also be used to monitor the progress of a
								 * normal http post/put request with large data
								 */
								// $scope.upload = $upload.http({...}) see
								// 88#issuecomment-31366487 for sample code.
							};

						} ]);

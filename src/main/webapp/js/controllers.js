var homearchiveControllers = angular.module('homearchiveControllers', [
		'fileServices', 'angularFileUpload', 'ngTable' ]);

homearchiveControllers.controller('FileListCtrl', [
		'$scope',
		'FileService',
		'ngTableParams',
		function($scope, FileService, ngTableParams) {

			FileService.query(function(data) {
				$scope.files = data;
				console.log(JSON.stringify($scope.files));
				$scope.tableParams = new ngTableParams({
					page : 1, // show first page
					count : 10
				// count per page
				}, {
					total : data.length, // length of data
					getData : function($defer, params) {
						$defer.resolve(data.slice((params.page() - 1)
								* params.count(), params.page()
								* params.count()));
					}
				});
			});

		} ]);

homearchiveControllers.controller('DownloadCtrl', [ '$scope', 'FileService',
		function($scope, FileService) {
			FileService.get({
				id : "543e75803004bdb5fdd82895"
			}, function(result) {
				// $scope.document = result;
			});

		} ]);

homearchiveControllers.controller('UploadCtrl', [
		'$scope',
		'$upload',
		function($scope, $upload) {

			$scope.onFileSelect = function($files) {
				// $files: an array of files selected, each file has name, size,
				// and type.
				for (var i = 0; i < $files.length; i++) {
					var file = $files[i];
					$scope.upload = $upload.upload({
						url : '/homearchive/rs/findFiles', // upload.php
															// script, node.js
															// route, or servlet
															// url
						method : 'POST',
						// headers: {'header-key': 'header-value'},
						// withCredentials: true,
						data : {
							tags : $scope.tags
						},
						file : file, // or list of files ($files) for html5
										// only
					// fileName: 'doc.jpg' or ['1.jpg', '2.jpg', ...] // to
					// modify the name of the file(s)
					// customize file formData name ('Content-Disposition'),
					// server side file variable name.
					// fileFormDataName: myFile, //or a list of names for
					// multiple files (html5). Default is 'file'
					// customize how data is added to formData. See
					// #40#issuecomment-28612000 for sample code
					// formDataAppender: function(formData, key, val){}
					}).progress(
							function(evt) {
								console.log('percent: '
										+ parseInt(100.0 * evt.loaded
												/ evt.total));
							}).success(function(data, status, headers, config) {
						// file is uploaded successfully
						console.log(data);
					});
					// .error(...)
					// .then(success, error, progress);
					// access or attach event listeners to the underlying
					// XMLHttpRequest.
					// .xhr(function(xhr){xhr.upload.addEventListener(...)})
				}
				/*
				 * alternative way of uploading, send the file binary with the
				 * file's content-type. Could be used to upload files to
				 * CouchDB, imgur, etc... html5 FileReader is needed. It could
				 * also be used to monitor the progress of a normal http
				 * post/put request with large data
				 */
				// $scope.upload = $upload.http({...}) see
				// 88#issuecomment-31366487 for sample code.
			};

		} ]);

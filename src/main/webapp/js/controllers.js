var homearchiveControllers = angular.module('homearchiveControllers', [
		'fileServices', 'angularFileUpload', 'ngTable' ]);

homearchiveControllers.controller('FileListCtrl', [
		'$scope',
		'FileService',
		'ngTableParams',
		function($scope, FileService, ngTableParams) {
			$scope.files = [];
			$scope.tableParams = new ngTableParams({
				page : 1, // show first page
				count : 10
			// count per page
			}, {
				total : $scope.files.length, // length of data
				getData : function($defer, params) {
					params.total($scope.files.length);
					$defer.resolve($scope.files.slice((params.page() - 1)
							* params.count(), params.page() * params.count()));
				}

			});

			$scope.submit = function() {

				FileService.query({

					fileName : $scope.fileName,
					startDate: $scope.startDate,
					endDate: $scope.endDate,
					tag1: $scope.tag1,
					tag2: $scope.tag2,
					tag3: $scope.tag3
					
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

			$scope.remove = function(file) {
				console.log("Removing file with id: " + file.id);
				FileService.remove({
					id : file.id
				}).$promise.then(function(){
					var idx = $scope.files.indexOf(file);
					 $scope.files.splice(idx, 1);
					 console.log("removed from scope, idx: " + $scope.files.indexOf(file));
					 $scope.tableParams.reload();
				})
					
				
				
			}

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

homearchiveControllers.controller('StartDateCtrl', function ($scope) {
	  $scope.today = function() {
	    $scope.$parent.startDate = new Date();
	  };
	  $scope.today();

	  $scope.clear = function () {
	    $scope.$parent.stratDate = null;
	  };

	  // Disable weekend selection
	  $scope.disabled = function(date, mode) {
	    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
	  };

	  $scope.toggleMin = function() {
	    $scope.minDate = $scope.minDate ? null : new Date();
	  };
	  $scope.toggleMin();
	  
	  $scope.toggleMax = function() {
		    $scope.maxDate = $scope.maxDate ? null : new Date();
		  };
		  $scope.toggleMax();
	  

	  $scope.open = function($event,opened) {
		    $event.preventDefault();
		    $event.stopPropagation();

		    $scope.opened = true;
		  };

	  $scope.dateOptions = {
	    formatYear: 'yy',
	    startingDay: 1
	  };

	  $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	  $scope.format = $scope.formats[0];
	});

homearchiveControllers.controller('EndDateCtrl', function ($scope) {
	  $scope.today = function() {
	    $scope.$parent.endDate = new Date();
	  };
	  $scope.today();

	  $scope.clear = function () {
	    $scope.$parent.endDate = null;
	  };

	  // Disable weekend selection
	  $scope.disabled = function(date, mode) {
	    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
	  };

	  $scope.toggleMin = function() {
	    $scope.minDate = $scope.minDate ? null : new Date();
	  };
	  $scope.toggleMin();
	  
	  $scope.toggleMax = function() {
		    $scope.maxDate = $scope.maxDate ? null : new Date();
		  };
		  $scope.toggleMax();
	  

	  $scope.open = function($event,opened) {
		    $event.preventDefault();
		    $event.stopPropagation();

		    $scope.opened = true;
		  };

	  $scope.dateOptions = {
	    formatYear: 'yy',
	    startingDay: 1
	  };

	  $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	  $scope.format = $scope.formats[0];
	});

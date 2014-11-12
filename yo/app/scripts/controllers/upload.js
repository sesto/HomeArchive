'use strict';
	/* jshint ignore:start */
/**
 * @ngdoc function
 * @name homearchiveApp.controller:UploadCtrl
 * @description # UploadCtrl Controller of the homearchiveApp
 */
angular.module('homearchiveApp').controller('UploadCtrl',
		[ '$scope', 'FileUploader', function($scope, FileUploader) {

			$scope.uploader = new FileUploader({
				url : 'rs/findFiles'
			});

			$scope.uploader.onBeforeUploadItem = function(item) {
				var data = [ {
					description : $scope.description
				} ]
				console.log("File description :" + angular.toJson(data));
				Array.prototype.push.apply(item.formData, data);
			};
		} ]);

/* jshint ignore:end */

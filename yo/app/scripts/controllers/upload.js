'use strict';

/**
 * @ngdoc function
 * @name homearchiveApp.controller:UploadCtrl
 * @description # UploadCtrl Controller of the homearchiveApp
 */
angular.module('homearchiveApp').controller('UploadCtrl',
		[ '$scope', 'FileUploader', function($scope, FileUploader) {
			$scope.tags = [];
			$scope.addTag = function() {

				$scope.tags.push($scope.tag);
				$scope.tag = '';
				$scope.removeTag = function(index) {
					$scope.tags.splice(index, 1);
				};
			};
			console.log("Entered tags" + $scope.tags);

			$scope.uploader = new FileUploader({
				url : 'rs/findFiles'
			});
			var data = [ {
				tags : $scope.tags
			} ]
			$scope.uploader.onBeforeUploadItem = function(item) {
				Array.prototype.push.apply(item.formData, data);
			};
		} ]);
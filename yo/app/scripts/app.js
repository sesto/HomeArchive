'use strict';

/**
 * @ngdoc overview
 * @name homearchiveApp
 * @description # homearchiveApp
 * 
 * Main module of the application.
 */
angular.module('homearchiveApp',
		[ 'ngRoute',
		  'fileServices', 'angularFileUpload', 'ngTable', 'ui.bootstrap' ])
		.config(function($routeProvider) {
			$routeProvider.when('/upload', {
				templateUrl : 'views/upload.html',
				controller : 'UploadCtrl'
			}).when('/search', {
				templateUrl : 'views/filelist.html',
				controller : 'FileListCtrl'
			}).otherwise({
				redirectTo : '/'
			});
		});

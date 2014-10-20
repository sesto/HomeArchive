var homearchiveApp = angular.module('homearchiveApp', [ 'homearchiveControllers', 'ngRoute', 'ui.bootstrap']);
homearchiveApp.config(['$routeProvider',
                    function($routeProvider) {
                      $routeProvider.
                        when('/search', {
                          templateUrl: 'partials/filelist.html',
                          controller: 'FileListCtrl'
                        }).
                        when('/upload', {
                          templateUrl: 'partials/upload.html',
                          controller: 'UploadCtrl'
                        }).
                        otherwise({
                          redirectTo: '/'
                        });
                    }]);
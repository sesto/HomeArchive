'use strict';
var appConfig;
/**
 * @ngdoc overview
 * @name homearchiveApp
 * @description # homearchiveApp
 *
 * Main module of the application.
 */
angular.module('homearchiveApp',
  ['ngRoute',
    'fileServices', 'userService', 'angularFileUpload', 'ngTable',  'ngCookies','ui.bootstrap'])
  .config([ '$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {
    $routeProvider.when('/upload', {
      templateUrl: 'views/upload.html',
      controller: 'UploadCtrl'
    }).when('/search', {
      templateUrl: 'views/filelist.html',
      controller: 'FileListCtrl'
    }).when('/login', {
      templateUrl: 'views/login.html',
      controller: 'LoginCtrl'
    }).otherwise({
      redirectTo: '/'
    });

   // $locationProvider.hashPrefix('!');

    /* Register error provider that shows message on failed requests or redirects to login page on
     * unauthenticated requests */
    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
        return {
          'responseError': function (rejection) {
            var status = rejection.status;
            var config = rejection.config;
            var method = config.method;
            var url = config.url;

            if (status == 401) {
              $location.path("/login");
            } else {
              $rootScope.error = method + " on " + url + " failed with status " + status;
            }

            return $q.reject(rejection);
          }
        };
      }
    );

    /* Registers auth token interceptor, auth token is either passed by header or by query parameter
     * as soon as there is an authenticated user */
    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
        return {
          'request': function(config) {
            var isRestCall = config.url.indexOf('rs') == 0;
            if (isRestCall && angular.isDefined($rootScope.authToken)) {
              var authToken = $rootScope.authToken;
              if (appConfig.useAuthTokenHeader) {
                config.headers['X-Auth-Token'] = authToken;
              } else {
                config.url = config.url + "?token=" + authToken;
              }
            }
            return config || $q.when(config);
          }
        };
      }
    );


  }]).run(function($rootScope, $location, $cookieStore, $route, UserService) {
    /* Reset error when a new view is loaded */
    $rootScope.$on('$viewContentLoaded', function() {
      delete $rootScope.error;
    });
    /* isAuth helper */
    $rootScope.isAuth = function() {
      return ($rootScope.authToken !== undefined);
    };
    /* hasRole helper function */
    $rootScope.hasRole = function(role) {
      if ($rootScope.user === undefined) {
        return false;
      }

      if ($rootScope.user.roles[role] === undefined) {
        return false;
      }

      return $rootScope.user.roles[role];
    };
    /* logout */
    $rootScope.logout = function() {
      delete $rootScope.user;
      delete $rootScope.authToken;
      $cookieStore.remove('authToken');
      $location.path('/login');
    };

    $rootScope.reload = function() {
      $route.reload();
    };

    /* Try getting valid user from cookie or go to login page */
    var originalPath = $location.path();
    $location.path('/login');
    var authToken = $cookieStore.get('authToken');
    if (authToken !== undefined) {
      console.log('AuthToken retrieved from cookie store');
      $rootScope.authToken = authToken;
      UserService.current(function(user) {
        $rootScope.user = user;
        $location.path(originalPath);
      });
    } else {
      console.log('No AuthToken in cookie store');
    }

    $rootScope.initialized = true;
  });








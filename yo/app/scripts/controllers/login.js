'use strict';

/**
 * @ngdoc function
 * @name homearchiveApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the homearchiveApp
 */
angular.module('homearchiveApp')
  .controller('LoginCtrl', ['$scope', '$rootScope', '$location', '$cookieStore', 'UserService', function($scope, $rootScope, $location, $cookieStore, UserService) {
    $rootScope.initialLogin=1;
    $scope.rememberMe = false;
    $scope.login = function() {
      console.log('Login start for '+$scope.username);
      $rootScope.initialLogin = 0;
      var params=$.param({username: $scope.username, password: $scope.password}); // jshint ignore:line
      UserService.authenticate(params, function(authenticationResult) {
        var authToken = authenticationResult.token;
        $rootScope.authToken = authToken;

        console.log('Auth token received:'+authToken);

        if ($scope.rememberMe) {
          $cookieStore.put('authToken', authToken);
        }

        UserService.current(function(user) {
          console.log('Current user:'+angular.toJson(user));

          $rootScope.user = user;
          $location.path('/');
        });
      });
    };
  }]);


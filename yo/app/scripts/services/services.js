'use strict';

var fileServices = angular.module('fileServices', ['ngResource']);

fileServices.factory('FileService', ['$resource', function ($resource) {
  return $resource('rs/fileService/:id', {id: '@id'},
    {
      update: {
        method: 'PUT'
      }
    });
}]);

var userService = angular.module('userService', ['ngResource']);

userService.factory('UserService', ['$resource', function ($resource) {

  return $resource('rs/user/:action', {},
    {
      authenticate: {
        method: 'POST',
        params: {'action' : 'authenticate'},
        headers : {'Content-Type': 'application/x-www-form-urlencoded'}
      },
      current: {
        method: 'GET'
      }
    }
  );
}
]);

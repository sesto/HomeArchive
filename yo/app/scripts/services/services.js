var fileServices = angular.module('fileServices', [ 'ngResource' ]);

fileServices.factory('FileService', [ '$resource', function($resource) {
	return $resource('/homearchive/rs/findFiles/:id', {});
} ]);

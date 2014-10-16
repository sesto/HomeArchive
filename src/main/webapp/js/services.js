var fileServices = angular.module('fileServices', [ 'ngResource' ]);

fileServices.factory('File', [ '$resource', function($resource) {
	return $resource('/homearchive/rs/findFiles/:id');
} ]);



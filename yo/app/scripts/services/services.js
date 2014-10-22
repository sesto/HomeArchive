var fileServices = angular.module('fileServices', [ 'ngResource' ]);

fileServices.factory('FileService', [ '$resource', function($resource) {
	return $resource('http://localhost:8080/homearchive/rs/findFiles/:id', {id:'@id'},
			{
		update:{
			method:'PUT'
		}
			});
} ]);

'use strict';

var fileServices = angular.module('fileServices', [ 'ngResource' ]);

fileServices.factory('FileService', [ '$resource', function($resource) {
	return $resource('rs/findFiles/:id', {id:'@id'},
			{
		update:{
			method:'PUT'
		}
			});
} ]);

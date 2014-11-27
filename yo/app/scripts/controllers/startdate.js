'use strict';

/**
 * @ngdoc function
 * @name homearchiveApp.controller:StartdateCtrl
 * @description # StartdateCtrl Controller of the homearchiveApp
 */
angular.module('homearchiveApp')
		.controller(
				'StartdateCtrl', ['$scope',
				function($scope) {

					$scope.clear = function() {
						$scope.$parent.stratDate = null;
					};

					// Disable weekend selection
					$scope.disabled = function(date, mode) {
						return (mode === 'day' && (date.getDay() === 0 || date
								.getDay() === 6));
					};

					$scope.toggleMin = function() {
						$scope.minDate = $scope.minDate ? null : new Date();
					};
					$scope.toggleMin();

					$scope.toggleMax = function() {
						$scope.maxDate = $scope.maxDate ? null : new Date();
					};
					$scope.toggleMax();

					$scope.open = function($event) {
						$event.preventDefault();
						$event.stopPropagation();

						$scope.opened = true;
					};

					$scope.dateOptions = {
						formatYear : 'yy',
						startingDay : 1
					};

					$scope.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd',
							'dd.MM.yyyy', 'shortDate' ];
					$scope.format = $scope.formats[0];
				}]);

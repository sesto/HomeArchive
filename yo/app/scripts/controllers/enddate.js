'use strict';

/**
 * @ngdoc function
 * @name homearchiveApp.controller:EnddateCtrl
 * @description # EnddateCtrl Controller of the homearchiveApp
 */
angular.module('homearchiveApp')
		.controller(
				'EnddateCtrl',
				function($scope) {
					// $scope.today = function() {
					// $scope.$parent.endDate = new Date();
					// };
					// $scope.today();

					$scope.clear = function() {
						$scope.$parent.endDate = null;
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
				});

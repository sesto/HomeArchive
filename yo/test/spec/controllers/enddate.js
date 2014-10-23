'use strict';

describe('Controller: EnddateCtrl', function () {

  // load the controller's module
  beforeEach(module('homearchiveApp'));

  var EnddateCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    EnddateCtrl = $controller('EnddateCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});

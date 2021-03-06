'use strict';

describe('Controller: StartdateCtrl', function () {

  // load the controller's module
  beforeEach(module('homearchiveApp'));

  var StartdateCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    StartdateCtrl = $controller('StartdateCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});

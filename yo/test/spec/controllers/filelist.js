'use strict';

describe('Controller: FilelistctrlCtrl', function () {

  // load the controller's module
  beforeEach(module('homearchiveApp'));

  var FilelistctrlCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    FilelistctrlCtrl = $controller('FilelistctrlCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});

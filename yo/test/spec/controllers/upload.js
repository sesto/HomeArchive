'use strict';

describe('Controller: UploadctrlCtrl', function () {

  // load the controller's module
  beforeEach(module('homearchiveApp'));

  var UploadctrlCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    UploadctrlCtrl = $controller('UploadctrlCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});

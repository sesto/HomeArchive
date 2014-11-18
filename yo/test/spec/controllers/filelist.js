'use strict';

describe('Controller: FilelistCtrl', function () {

  // load the controller's module
  beforeEach(module('homearchiveApp'));

  var FilelistCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller) {
    scope = {};
    FilelistCtrl = $controller('FileListCtrl', {
      $scope: scope
    });
  }));

  it('scope.file should be defined', function () {
    expect(scope.file).toBeDefined();
  });

  it('scope.file.metadata should be defined', function () {
    expect(scope.file.metadata).toBeDefined();
  });

  it('scope.files array should be defined with length 0', function () {
    expect(scope.files.length).toBe(0);
  });

  it('scope.tableParams array should be defined', function () {
    expect(scope.tableParams).toBeDefined();
  });

});

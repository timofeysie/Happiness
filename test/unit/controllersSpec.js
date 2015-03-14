'use strict';

/* jasmine specs for controllers go here */
describe('Happiness controllers', function() {

  beforeEach(function(){
    this.addMatchers({
      toEqualData: function(expected) {
        return angular.equals(this.actual, expected);
      }
    });
  });

  beforeEach(module('happinessApp'));
  beforeEach(module('happinessServices'));

  describe('DefaultController', function(){
    var scope, ctrl, $httpBackend;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
      $httpBackend = _$httpBackend_;
      $httpBackend.expectGET('data/galleries.json').
          respond([{id: 'default'}]);

      scope = $rootScope.$new();
      ctrl = $controller('DefaultController', {$scope: scope});
    }));


    it('should create gallery with id default', function() {
      expect(scope.phones).toEqualData([]);
      $httpBackend.flush();

      expect(scope.phones).toEqualData(
          [{id: 'default'}]);
    });
  });

});

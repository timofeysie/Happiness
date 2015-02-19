'use strict';

/* App Module */

var phonecatApp = angular.module('phonecatApp', [
  'ngRoute',
  'phonecatAnimations',
  'phonecatControllers',
  'phonecatFilters',
  'phonecatServices'
]);

phonecatApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/default', {
        templateUrl: 'partials/default.html',
        controller: 'DefaultController'
      }).
      otherwise({
        redirectTo: '/default'
      });
  }]);

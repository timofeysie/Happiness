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
      when('/main', {
        templateUrl: 'partials/main.html',
        controller: 'MainController'
      }).
      when('/default', {
        templateUrl: 'partials/default.html',
        controller: 'DefaultController'
      }).
      otherwise({
        redirectTo: '/main'
      });
  }]);

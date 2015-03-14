'use strict';
/* App Module */
var happinessApp = angular.module('happinessApp', [
  'ngRoute',
  'ngResource',
  'ngTouch',
  'happinessAnimations',
  'happinessControllers',
  'happinessFilters',
  'happinessServices',
  'happinessApp.profileController',
  'happinessApp.profileServices'
]);

happinessApp.config(['$routeProvider',
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
      when('/about', {
        templateUrl: 'partials/about.html',
        controller: 'AboutController'
      }).
      when('/developer', {
        templateUrl: 'partials/developer.html',
        controller: 'ProfileController'
      }).
      otherwise({
        redirectTo: '/main'
      });
  }]);

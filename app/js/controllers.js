'use strict';

/* Controllers */

var phonecatControllers = angular.module('phonecatControllers', []);

phonecatControllers.controller('MainController', ['$scope', 'Gallery',
  function($scope, Gallery) {
    $scope.galleries = Gallery.query();
  }]);

phonecatControllers.controller('DefaultController', ['$scope', '$routeParams', 
  '$location', 'HappyOrSadService',
  'PositiveService', 'NegativeService', '$timeout',
  function($scope, $routeParams, $location, HappyOrSadService, 
    PositiveService, NegativeService, $timeout) {
    // default settings for button one positive, button two negative
    $scope.imageOneUrl = '';
    $scope.imageTwoUrl = '';
    $scope.button_one = 'OK';
    $scope.button_two = 'OK';
    $scope.answer = 'one';
    $scope.count = 0;
    $scope.end = 0;
    console.log('Score '+$scope.count);
    var randy = Math.random()*10;
    console.log('Randy '+(randy)); // Choose which button will be positive.
    if (randy < 5)
    { 
      // Button one is happy, two is bad
      console.log('button_one is happy'); 
      getPicture('positive_one');
    } else
    { 
      // Button one is sad, button two is happy
      console.log('button_two is happy'); 
      getPicture('positive_two');
      $scope.answer = 'two';
    }
    $scope.showImageOne = true;
    $scope.showImageTwo = true;

    // Called when the user chooses an image
    $scope.increment = function(type) {
      var next_randy = Math.random();
      if ((type == 'one' && $scope.answer == 'one') || (type == 'two' && $scope.answer == 'two')) {
        hideImage(type);
      } else
      { // response for trying to choose the negative, change the negative button   
        if (($scope.answer == 'one')) { 
            $scope.button_two = 'Not OK';
          } else {
            $scope.button_one = 'Not OK';
          }
          console.log("not OK");
      }
    }

    // Set up the images for the next round
    function nextRound(picture_type)
    {
      var next_randy = Math.random();
      $scope.button_one = 'OK';
      $scope.button_two = 'OK';
      $scope.count = $scope.count +1;
      console.log("Correct: count "+$scope.count);
      // correct response: next round button one is happy, two is sad
      if ((next_randy*100) > 50) {
        $scope.answer = 'one';
        console.log('button_one is happy'); 
        getPicture('positive_one');
      } else {   
        // Button one is sad, button two is happy
        $scope.answer = 'two';
        console.log('button_two is happy'); 
        getPicture('positive_two');
      }
      if ($scope.count > 17) {
        console.log("The End");
        alert("The End");
        $location.path( "/main" );
      }
    }

    function getPicture(picture_type)
    {
      console.log('picture type = '+picture_type);
      if (picture_type == 'positive_one')
      {
        PositiveService.query(function (response) {
          angular.forEach(response, function (item) {
            if (item.order == $scope.count) {
              $scope.imageOneUrl = item.imageUrl;
              $scope.showImageOne = true;
              console.log('A. +1 '+picture_type);
            }
          });
        });
        NegativeService.query(function (response) {
          angular.forEach(response, function (item) {
            if (item.order == $scope.count) {
              $scope.imageTwoUrl = item.imageUrl;
              $scope.showImageTwo = true;
              console.log('A. -2 '+picture_type);
            }
          });
        });
      } else {
        PositiveService.query(function (response) {
          angular.forEach(response, function (item) {
            if (item.order == $scope.count) {
              $scope.imageTwoUrl = item.imageUrl;
              $scope.showImageTwo = true;
              console.log('B. -2 '+picture_type);
            }
          });
        });
        NegativeService.query(function (response) {
          angular.forEach(response, function (item) {
            if (item.order == $scope.count) {
              $scope.imageOneUrl = item.imageUrl;
              $scope.showImageOne = true;
              console.log('B. -1 '+picture_type);
            }
          });
        });
      } 
    }
    /* Since the type is the answer, we must hide the opposite image. */
    function hideImage(type)
    {
      if (type == 'two')
      {
        $scope.showImageOne = false;
      } else
      {
        $scope.showImageTwo = false;
      }
      $timeout(callTimeout, 2000);
    }

    function callTimeout(type) {
      console.log("Timeout1 occurred");
      $scope.showImageOne = false;
      $scope.showImageTwo = false;
      nextRound(type);
    }
  }
]);

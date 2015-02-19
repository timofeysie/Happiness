'use strict';

/* Controllers */

var phonecatControllers = angular.module('phonecatControllers', []);

phonecatControllers.controller('PhoneListCtrl', ['$scope', 'Phone',
  function($scope, Phone) {
    $scope.phones = Phone.query();
    $scope.orderProp = 'age';
  }]);

phonecatControllers.controller('PhoneDetailCtrl', ['$scope', '$routeParams', 'Phone',
  function($scope, $routeParams, Phone) {
    $scope.phone = Phone.get({phoneId: $routeParams.phoneId}, function(phone) {
      $scope.mainImageUrl = phone.images[0];
    });

    $scope.setImage = function(imageUrl) {
      $scope.mainImageUrl = imageUrl;
    }
  }]);

phonecatControllers.controller('DefaultController', ['$scope', '$routeParams', 'HappyOrSadService',
  'PositiveService', 'NegativeService',
  function($scope, $routeParams, HappyOrSadService, PositiveService, NegativeService) {
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

    $scope.increment = function(type) {
      var next_randy = Math.random();
      if ((type == 'one' && $scope.answer == 'one') || (type == 'two' && $scope.answer == 'two')) {
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
      } else
      { // response for trying to choose the negative, change the negative button   
        if (($scope.answer == 'one')) { 
            $scope.button_two = 'Not OK';
          } else {
            $scope.button_one = 'Not OK';
          }
          console.log("not OK");
      }
      if ($scope.count > 18) {
        console.log("The End");
        alert("The End");
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
              console.log('A. +1 '+picture_type);
            }
          });
        });
        NegativeService.query(function (response) {
          angular.forEach(response, function (item) {
            if (item.order == $scope.count) {
              $scope.imageTwoUrl = item.imageUrl;
              console.log('A. -2 '+picture_type);
            }
          });
        });
      } else {
        PositiveService.query(function (response) {
          angular.forEach(response, function (item) {
            if (item.order == $scope.count) {
              $scope.imageTwoUrl = item.imageUrl;
              console.log('B. -2 '+picture_type);
            }
          });
        });
        NegativeService.query(function (response) {
          angular.forEach(response, function (item) {
            if (item.order == $scope.count) {
              $scope.imageOneUrl = item.imageUrl;
              console.log('B. -1 '+picture_type);
            }
          });
        });
      } 
    }
  }
]);

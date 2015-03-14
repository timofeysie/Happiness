'use strict';
angular.module("happinessApp.profileController", [])
.controller('ProfileController',
    function ($scope, $rootScope, $route, ProfileServices, $log) {
    var professionalDetail = ProfileServices.getProfessionalDetail(),
        personalDetail = ProfileServices.getPersonalDetail();
    $scope.professional = {};
    $scope.personal = {};
    //Default menu button selected to true
    $scope.selected = true;
    /*Calls the Angular service to load  professional JSON data*/
    professionalDetail.get(function(jsonData){
       $scope.professional = jsonData;
       console.log("pro data "+$scope.professional.length);
    });
    /*Calls the Angular service to load  personal JSON data*/
    personalDetail.get(function(jsonData){
       $scope.personal = jsonData;
    });
    /*Method to change the user selection*/
    $scope.getDetail=function(event){
      $scope.selected = !$scope.selected;
    };
})


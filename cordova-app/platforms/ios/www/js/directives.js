'use strict';

angular.module('happinessApp')
.directive("responsiveSessionImage",
    ["$log","$window",
        function ($log, $window) {
            return {
                restrict:'E',
                replace:true,
                scope: {
                    'number': '=imgnumber',
                    'source': '=imgsource',
                    'alt': '=imagealt',
                },
                template: '<img \
            		id="{{button_id}}"\
            		height="{{modifiedHeight}}"\
            		ng-src="{{source}}"\
            		ng-click="{{increment}}"\
                	alt="{{respalt}}"/>',
                link : function(scope, element, attribute){
                    scope.width = $window.outerWidth;
                    scope.button_id = 'button_'+number+'_id';
                    scope.source = 'image'+number+'url';
                    scope.increment = 'increment('+number+')';
                    scope.$watch("width",function(newWidth,oldWidth){
                        $log.log("New width of window : ",newWidth);
                        if(newWidth <= 400){
                            scope.modifiedHeight = '175';
                        }else if(newWidth >400 && newWidth <=767){
                            scope.modifiedHeight= '300';
                        }else{
                            scope.modifiedHeight = '500';
                        }
                    });
                    angular.element($window).bind('resize',function(){
                        //Asking AngularJS to run digest cycle
                        scope.$apply(function(){
                            scope.width = $window.outerWidth;
                        })
                    });
                }};
        }])

.directive("responsiveImage",
    ["$log","$window",
        function ($log, $window) {
            return {
                restrict:'E',
                replace:true,
                scope: {
                    'respalt': '@imagealt',
                    'respsrc': '@imagesrc'
                },
                template: '<img class="profile-image"' +
                    'ng-src="{{modifiedsrc}}" alt="{{respalt}}"/>',
                link : function(scope, element, attribute){
                    scope.width = $window.outerWidth;
                    scope.$watch("width",function(newWidth,oldWidth){
                        $log.log("New width of window : ",newWidth);
                        if(newWidth <= 400){
                            scope.modifiedsrc = scope.respsrc +"?s=80";
                        }else if(newWidth >400 && newWidth <=767){
                            scope.modifiedsrc = scope.respsrc +"?s=150";
                        }else{
                            scope.modifiedsrc = scope.respsrc +"?s=250";
                        }
                    });
                    angular.element($window).bind('resize',function(){
                        //Asking AngularJS to run digest cycle
                        scope.$apply(function(){
                            scope.width = $window.outerWidth;
                        })
                    });
                }};
        }])

.directive("responsiveHeader",
    ["$log","$window",
        function ($log, $window) {
            return {
                restrict:'E',
                replace:true,
                scope:{
                    'respText':'@targettext'
                },
                template: "<p class='{{deviceSize}}'> {{respText}} </p>",
                link : function(scope, element, attribute){
                    scope.deviceSize = "largeDevice";
                    scope.width = $window.outerWidth;
                    scope.$watch("width",function(newWidth,oldWidth){
                        if(newWidth <= 400){
                            scope.deviceSize = "smallDevice"
                        }else if(newWidth >400 && newWidth <=767){
                            scope.deviceSize = "mediumDevice";
                        }else{
                            scope.deviceSize = "largeDevice";
                        }
                    });
                    angular.element($window).bind('resize',function(){
                        scope.$apply(function(){
                            scope.width = $window.outerWidth;
                        });
                    });
                }};
        }])

.directive("responsiveParagraph",
    ["$log","$window",
        function ($log, $window) {
            return {
                restrict:'E',
                replace:true,
                scope:{
                    'respPara':'@targetpara'
                },
                template: "<p class='aboutme {{paragraphSize}}'> {{respPara}} </p>",
                link : function(scope, element, attribute){
                    scope.paragraphSize = "largePara";
                    scope.width = $window.outerWidth;
                    scope.$watch("width",function(newWidth,oldWidth){
                        if(newWidth <= 400){
                            scope.paragraphSize = "smallPara"
                        }else if(newWidth >400 && newWidth <=767){
                            scope.paragraphSize = "mediumPara";
                        }else{
                            scope.paragraphSize = "largePara";
                        }
                    });
                    angular.element($window).bind('resize',function(){
                        scope.$apply(function(){
                            scope.width = $window.outerWidth;
                        });
                    });
               }};
        }])

.directive("responsiveList",
    ["$log","$window",
        function ($log, $window) {
            return {
                restrict:'E',
                replace:true,
                scope:{
                    'itemList':'=targetlist'
                },
                template: '<div class="item-list-container">'+
                          '<ol>'+
                          '<li ng-class="{smallText:isMorePresent}" ng-repeat="item in itemDisplayList">{{item}}</li>'+
                          '</ol>'+
                          '<button class="show-more" ng-show="isMorePresent" ' +
                           'ng-click="showMore(itemDisplayList)"><span>More...</span></button>'+
                          '</div>',
                link : function(scope, element, attribute){
                        scope.isMorePresent = false;

                        scope.$watch("itemList",function(newItemList,oldItemList){
                            scope.itemList = newItemList;
                            scope.height = $window.outerHeight;
                            scope.itemDisplayList = scope.itemList;
                        },true);
                        scope.$watch("height",function(newHeight,oldHeight){
                            var listLength = angular.isDefined(scope.itemList)?
                                             scope.itemList.length : 0 ;
                            if(newHeight <400 && listLength >2){
                                scope.isMorePresent = true;
                                scope.itemDisplayList = scope.itemList.slice(0,2);
                            }else if(newHeight >= 400 && newHeight <700 && listLength >3){
                                scope.isMorePresent = true;
                                scope.itemDisplayList = scope.itemList.slice(0,3);
                            }else{
                                scope.isMorePresent = false;
                                scope.itemDisplayList = scope.itemList;
                            }
                        });
                        angular.element($window).bind('resize',function(){
                            scope.$apply(function(){
                                scope.height = $window.outerHeight;
                            });
                        });
                        scope.showMore = function(initalList){
                           scope.itemDisplayList = scope.itemList;
                           scope.isMorePresent = false;
                        }
                }};
        }]);

'use strict';

/* Services */

var happinessServices = angular.module('happinessServices', ['ngResource']);

happinessServices.factory('Phone', ['$resource',
  	function($resource){
    	return $resource('phones/:phoneId.json', {}, {
      		query: {method:'GET', params:{phoneId:'phones'}, isArray:true}
    	});
}]);

happinessServices.factory('Gallery',
    function($resource){
        return $resource('data/galleries.json');
});

happinessServices.factory('PositiveService',
	function($resource){
    	return $resource('data/positive.json');
});

happinessServices.factory('NegativeService',
	function($resource){
    return $resource('data/negative.json');
});

happinessServices.factory('HappyOrSadService',
	function($resource){
    	return $resource('data/positive.json'), {}, {
    		query: function(type) {
        		if (type == 'happy') {
        			return $resource('data/psotive.json');
        		} else  {
        			return $resource('data/negative.json');
        		}
        	}
        }
    }
);
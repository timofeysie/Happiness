'use strict';

/* Services */

var phonecatServices = angular.module('phonecatServices', ['ngResource']);

phonecatServices.factory('Phone', ['$resource',
  	function($resource){
    	return $resource('phones/:phoneId.json', {}, {
      		query: {method:'GET', params:{phoneId:'phones'}, isArray:true}
    	});
}]);

phonecatServices.factory('PositiveService',
	function($resource){
    	return $resource('data/positive.json');
});

phonecatServices.factory('NegativeService',
	function($resource){
    return $resource('data/negative.json');
});

phonecatServices.factory('HappyOrSadService',
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
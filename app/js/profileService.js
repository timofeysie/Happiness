angular.module('happinessApp.profileServices', [])
.service('ProfileServices',["$resource", function($resource){
   return{
       getPersonalDetail : function(){
       	console.log("getPersonalDetail");
           return $resource("./data/personalDetail.json")
       },
       getProfessionalDetail : function(){
       	   console.log("getProfessionalDetail");
           return $resource("./data/professionalDetail.json")
       }
   }
}]);
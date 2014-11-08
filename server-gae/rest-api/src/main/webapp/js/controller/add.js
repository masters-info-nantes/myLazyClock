

var controller = angular.module('myLazyClock.controller.alarmClock.add', []);

controller.controller('myLazyClock.controller.alarmClock.add', ['$scope', 'GApi',
    function homeCtl($scope, GApi) {
    	$scope.submitAdd = function(){
        	GApi.get('link',{
          		alarmClockId: $scope.alarmClock.id
        	}, function(resp) {
                console.log("todos api sucessfully called");
                console.log(resp);
                alert(resp.id);
            });
        
    	}
    }
]);
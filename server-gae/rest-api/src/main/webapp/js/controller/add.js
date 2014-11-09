

var controller = angular.module('myLazyClock.controller.alarmClock.add', []);

controller.controller('myLazyClock.controller.alarmClock.add', ['$scope', 'GApi', '$state',
    function homeCtl($scope, GApi, $state) {
    	$scope.submitAdd = function(){
        	GApi.get('link',$scope.alarmClock, function(resp) {
                $state.go('webapp.alarmClockItem', {id : resp.id})
            });
        
    	}
    }
]);


var controller = angular.module('myLazyClock.controller.alarmClock.add', []);

controller.controller('myLazyClock.controller.alarmClock.add', ['$scope', 'GApi', '$state',
    function homeCtl($scope, GApi, $state) {
    	$scope.submitAdd = function(){
        	GApi.executeAuth('myLazyClock', 'alarmClock.link',$scope.alarmClock).then(function(resp) {
                $state.go('webapp.alarmClockItem', {id : resp.id})
            });
    	}
    }
]);
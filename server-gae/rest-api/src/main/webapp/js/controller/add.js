

var controller = angular.module('myLazyClock.controller.alarmClock.add', []);

controller.controller('myLazyClock.controller.alarmClock.add', ['$rootScope', '$scope', 'GApi', '$state',
    function homeCtl($rootScope, $scope, GApi, $state) {
    	$scope.submitAdd = function(){
        	GApi.executeAuth('myLazyClock', 'alarmClock.link',$scope.alarmClock).then(function(resp) {
                $rootScope.alarmClocks.push(resp);
                $state.go('webapp.alarmClockItem.view', {id : resp.id});
            });
    	}
    }
]);
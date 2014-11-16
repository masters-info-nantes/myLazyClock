var controller = angular.module('myLazyClock.controller.home', []);

controller.controller('myLazyClock.controller.home', ['$scope', '$timeout', 'GApi',
    function homeCtl($scope, $timeout, GApi) {
    	$scope.clock; // initialise the time variable
    	$scope.tickInterval = 1000 //ms

    	var tick = function() {
        	$scope.clock = Date.now() // get the current time
        	$timeout(tick, $scope.tickInterval); // reset the timer
    	}
    	$timeout(tick, $scope.tickInterval);

        GApi.executeAuth('myLazyClock', 'alarmClock.list').then(function(resp) {
                console.log("todos api suc");
                console.log(resp);
                $scope.alarmClocks = resp.items;
            });
        GApi.executeAuth('myLazyClock', 'alarmClock.item', {alarmClockId: 4785074604081152}).then( function(respt) {
                console.log("todos api sucessfully called");
                console.log(respt);
            });
    }
]);
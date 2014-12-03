var controller = angular.module('myLazyClock.controller.clock', []);

controller.controller('myLazyClock.controller.clock', ['$scope', '$timeout',
    function homeCtl($scope, $timeout) {
    	    	$scope.clock; // initialise the time variable
    	$scope.tickInterval = 1000 //ms

    	var tick = function() {
        	$scope.clock = Date.now() // get the current time
        	$timeout(tick, $scope.tickInterval); // reset the timer
    	}
    	$timeout(tick, $scope.tickInterval);
    }
]);
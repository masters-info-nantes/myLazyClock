var controller = angular.module('myLazyClock.controller.home', []);

controller.controller('myLazyClock.controller.home', ['$scope', '$localStorage', 'GApi',
    function homeCtl($scope, $localStorage, GApi) {
    	GApi.get('item', {alarmClockId: $localStorage.alarmClockId}, function(resp) {
			$scope.alarmClock = resp;
            $scope.$apply($scope.alarmClock);
		});
    }
]);


var controller = angular.module('myLazyClock.controller.alarmClock.add', []);

controller.controller('myLazyClock.controller.alarmClock.add', ['$rootScope', '$scope', 'GApi', '$state', 'PREPARATION_TIMES', 'RINGTONES',
    function homeCtl($rootScope, $scope, GApi, $state, PREPARATION_TIMES, RINGTONES) {
    	$scope.preparationTimes = PREPARATION_TIMES;
        $scope.ringtones = RINGTONES;
    	$scope.alarmClock = {};
    	$scope.alarmClock.color = '#2196F3';
    	$scope.submitAdd = function(){
    		$scope.alarmClock.preparationTime = $scope.alarmClock.preparationTime.time;
            $scope.alarmClock.ringtone = $scope.alarmClock.ringtone.file;
        	GApi.executeAuth('myLazyClock', 'alarmClock.link',$scope.alarmClock).then(function(resp) {
                $rootScope.alarmClocks.push(resp);
                $state.go('webapp.alarmClockItem.view.calendarlist', {id : resp.id});
            });
    	}

        $scope.locationOK = false;

        $scope.$on("locationAutocompleted", function(event, location) {
            $scope.locationOK = true;
        })

        $scope.editLocation = function() {
            $scope.locationOK = false;
        }
    }
]);
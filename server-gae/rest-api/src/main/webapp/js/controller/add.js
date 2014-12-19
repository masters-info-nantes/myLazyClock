

var controller = angular.module('myLazyClock.controller.alarmClock.add', []);

controller.controller('myLazyClock.controller.alarmClock.add', ['$rootScope', '$scope', 'GApi', '$state', 'PREPARATION_TIMES', 'RINGTONES',
    function homeCtl($rootScope, $scope, GApi, $state, PREPARATION_TIMES, RINGTONES) {
    	$scope.preparationTimes = PREPARATION_TIMES;
        $scope.ringtones = RINGTONES;
    	$scope.alarmClock = {};
    	$scope.alarmClock.color = '#2196F3';
    	$scope.submitAdd = function(){
            $scope.tempAlarmClock = angular.copy($scope.alarmClock);
    		$scope.tempAlarmClock.preparationTime = $scope.tempAlarmClock.preparationTime.time;
            $scope.tempAlarmClock.ringtone = $scope.tempAlarmClock.ringtone.file;
        	GApi.executeAuth('myLazyClock', 'alarmClock.link', $scope.tempAlarmClock).then(function(resp) {
                $rootScope.alarmClocks.push(resp);
                $state.go('webapp.alarmClockItem.view.calendarlist', {id : resp.id});
            }, function () {
                $scope.errorForm = true;
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
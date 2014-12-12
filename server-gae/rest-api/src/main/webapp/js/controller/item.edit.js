var controller = angular.module('myLazyClock.controller.alarmClock.item.edit', []);

controller.controller('myLazyClock.controller.alarmClock.item.edit', ['$scope', '$rootScope', 'GApi', '$state', 'PREPARATION_TIMES', 'RINGTONES',
    function homeCtl($scope, $rootScope, GApi, $state, PREPARATION_TIMES, RINGTONES) {
        $scope.preparationTimes = PREPARATION_TIMES;
        $scope.ringtones = RINGTONES;
        $scope.alarmClockTemp = {};
        $scope.$watch('alarmClock', function () {
            angular.copy($scope.alarmClock, $scope.alarmClockTemp);
            angular.forEach($scope.preparationTimes, function(value) {
                    if (value.time == $scope.alarmClockTemp.preparationTime) {
                        $scope.alarmClockTemp.preparationTime = value;
                    }
                        
                });
            angular.forEach($scope.ringtones, function(value) {
                console.log($scope.alarmClockTemp.ringtone);
                    if (value.file == $scope.alarmClockTemp.ringtone) {
                        $scope.alarmClockTemp.ringtone = value;
                    }
                        
                });
        });

        $scope.locationOK = true;

        $scope.$on("locationAutocompleted", function(event, location) {
            $scope.locationOK = true;
        })

        $scope.editLocation = function() {
            $scope.locationOK = false;
        }

        $scope.submitUpdate = function(){
            $scope.alarmClockTemp.preparationTime = $scope.alarmClockTemp.preparationTime.time;
            $scope.alarmClockTemp.ringtone = $scope.alarmClockTemp.ringtone.file;
            GApi.executeAuth('myLazyClock', 'alarmClock.update', $scope.alarmClockTemp).then( function(resp) {
                $scope.$parent.alarmClock = resp;
                for(var i= 0; i < $rootScope.alarmClocks.length; i++){
                    if($rootScope.alarmClocks[i]['id'] == $scope.alarmClock.id)
                        $rootScope.alarmClocks[i]['name'] = $scope.alarmClock.name;
                }
                $state.go('webapp.alarmClockItem.view.calendarlist',{ id: $scope.alarmClock.id });
            });
        };
    }
]);
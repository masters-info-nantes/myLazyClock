var controller = angular.module('myLazyClock.controller.alarmClock.item.edit', []);

controller.controller('myLazyClock.controller.alarmClock.item.edit', ['$scope', '$rootScope', 'GApi', '$state', 'PREPARATION_TIMES',
    function homeCtl($scope, $rootScope, GApi, $state, PREPARATION_TIMES) {
        $scope.preparationTimes = PREPARATION_TIMES;
        $scope.alarmClockTemp = {};
        $scope.$watch('alarmClock', function () {
            angular.copy($scope.alarmClock, $scope.alarmClockTemp);
            angular.forEach($scope.preparationTimes, function(value) {
                    if (value.time == $scope.alarmClockTemp.preparationTime) {
                        $scope.alarmClockTemp.preparationTime = value;
                    }
                        
                });
        });

        $scope.submitUpdate = function(){
            $scope.alarmClockTemp.preparationTime = $scope.alarmClockTemp.preparationTime.time;
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
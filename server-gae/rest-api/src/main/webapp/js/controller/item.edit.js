var controller = angular.module('myLazyClock.controller.alarmClock.item.edit', []);

controller.controller('myLazyClock.controller.alarmClock.item.edit', ['$scope', '$rootScope', 'GApi', '$state',
    function homeCtl($scope, $rootScope, GApi, $state) {
        $scope.alarmClockTemp = {};
        $scope.$watch('alarmClock', function () {
            angular.copy($scope.alarmClock, $scope.alarmClockTemp);
        });
        $scope.submitUpdate = function(){
            GApi.executeAuth('myLazyClock', 'alarmClock.update', $scope.alarmClockTemp).then( function(resp) {
                $scope.$parent.alarmClock = resp;
                for(var i= 0; i < $rootScope.alarmClocks.length; i++){
                    if($rootScope.alarmClocks[i]['id'] == $scope.alarmClock.id)
                        $rootScope.alarmClocks[i]['name'] = $scope.alarmClock.name;
                }
                $state.go('webapp.alarmClockItem.view',{ id: $scope.alarmClock.id });
            });
        };
    }
]);
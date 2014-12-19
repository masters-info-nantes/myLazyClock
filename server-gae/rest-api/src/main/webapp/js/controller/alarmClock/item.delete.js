var controller = angular.module('myLazyClock.controller.alarmClock.item.delete', []);

controller.controller('myLazyClock.controller.alarmClock.item.delete', ['$scope', '$rootScope', 'GApi', '$state',
    function homeCtl($scope, $rootScope, GApi, $state) {
        $scope.submitDelete = function(){
            GApi.executeAuth('myLazyClock', 'alarmClock.unlink', {'alarmClockId' : $scope.alarmClock.id}).then( function(resp) {
                for(var i= 0; i < $rootScope.alarmClocks.length; i++){
                    if($rootScope.alarmClocks[i]['id'] == $scope.alarmClock.id) {
                        if (i > -1) {
                            $rootScope.alarmClocks.splice(i--, 1);
                        }
                    }
                }
                $state.go('webapp.home');
            });
        };
    }
]);
var controller = angular.module('myLazyClock.controller.alarmClock.item', []);

controller.controller('myLazyClock.controller.alarmClock.item', ['$scope', 'GApi', '$stateParams',
    function homeCtl($scope, GApi, $stateParams) {
        GApi.executeAuth('myLazyClock', 'alarmClock.item', {alarmClockId: $stateParams.id}).then( function(resp) {
                $scope.alarmClock = resp;
            });
        
    }
]);
var controller = angular.module('myLazyClock.controller.alarmClock.item', []);

controller.controller('myLazyClock.controller.alarmClock.item', ['$scope', 'GApi', '$stateParams',
    function homeCtl($scope, GApi, $stateParams) {
        GApi.executeAuth('myLazyClock', 'alarmClock.item', {alarmClockId: $stateParams.id}).then( function(resp) {
                console.log("todos api sucessfully called");
                console.log(resp);
                $scope.alarmClock = resp;
            });
        GApi.executeAuth('calendar', 'calendarList.list').then( function(resp) {
                console.log("todos api sucessfully called");
                console.log(resp);
               // $scope.alarmClock = resp;
            });
        
    }
]);
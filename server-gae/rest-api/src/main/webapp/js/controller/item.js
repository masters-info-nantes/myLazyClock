var controller = angular.module('myLazyClock.controller.alarmClock.item', []);

controller.controller('myLazyClock.controller.alarmClock.item', ['$scope', 'GApi', '$stateParams',
    function homeCtl($scope, GApi, $stateParams) {
        GApi.get('alarmClock.item', {alarmClockId: $stateParams.id}, function(resp) {
                console.log("todos api sucessfully called");
                console.log(resp);
                $scope.alarmClock = resp;
                $scope.$apply($scope.alarmClock);
            });
    }
]);
var controller = angular.module('myLazyClock.controller.alarmClock.item', []);

controller.controller('myLazyClock.controller.alarmClock.item', ['$scope', '$rootScope', 'Api', '$stateParams',
    function homeCtl($scope, $rootScope, Api, $stateParams) {
        Api.post('item', {alarmClockId: $stateParams.id}, function(resp) {
                console.log("todos api sucessfully called");
                console.log(resp);
                $scope.alarmClock = resp;
                $scope.$apply($scope.alarmClock);
            });
    }
]);
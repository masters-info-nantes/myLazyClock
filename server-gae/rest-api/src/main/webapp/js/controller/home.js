var controller = angular.module('myLazyClock.controller.home', []);

controller.controller('myLazyClock.controller.home', ['$scope', '$timeout', 'GApi',
    function homeCtl($scope, $timeout, GApi) {

        GApi.executeAuth('myLazyClock', 'alarmClock.list').then(function(resp) {
                $scope.alarmClocks = resp.items;
            });
    }
]);
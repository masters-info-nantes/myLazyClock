var controller = angular.module('myLazyClock.controller.home', []);

controller.controller('myLazyClock.controller.home', ['$scope', 'GApi',
    function homeCtl($scope, GApi) {
        GApi.get('list', function(resp) {
                console.log("todos api sucessfully called");
                console.log(resp);
                $scope.alarmClocks = resp.items;
                $scope.$apply($scope.alarmClocks);
            });
    }
]);
var controller = angular.module('myLazyClock.controller.home', []);

controller.controller('myLazyClock.controller.home', ['$scope', '$rootScope', 'Api',
    function homeCtl($scope, $rootScope, Api) {
        Api.get('list', function(resp) {
                console.log("todos api sucessfully called");
                console.log(resp);
                $scope.alarmClocks = resp.items;
                $scope.$apply($scope.alarmClocks);
            });
    }
]);
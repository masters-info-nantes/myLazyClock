var controller = angular.module('myLazyClock.controller.alarmClock.generate', []);

controller.controller('myLazyClock.controller.alarmClock.generate', ['$scope', '$rootScope', 'Api',
    function homeCtl($scope, $rootScope, Api) {
    	Api.get('generate', function(resp) {
                console.log("td");
                console.log(resp);
                alert(resp.id);
            });
    }
]);
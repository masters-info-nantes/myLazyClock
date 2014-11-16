var controller = angular.module('myLazyClock.controller.alarmClock.generate', []);

controller.controller('myLazyClock.controller.alarmClock.generate', ['$scope', 'GApi',
    function homeCtl($scope, GApi) {
    	GApi.execute('myLazyClock', 'alarmClock.generate').then(function(resp) {
                console.log("td");
                console.log(resp);
                alert(resp.id);
            });
    }
]);
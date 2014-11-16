var controller = angular.module('myLazyClock.controller.page', []);

controller.controller('myLazyClock.controller.page', ['$rootScope', 'GApi',
    function homeCtl($rootScope, GApi) {
        GApi.executeAuth('myLazyClock', 'alarmClock.list').then(function(resp) {
                $rootScope.alarmClocks = resp.items;
            });
    }
]);
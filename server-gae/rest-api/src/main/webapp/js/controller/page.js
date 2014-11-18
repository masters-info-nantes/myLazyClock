var controller = angular.module('myLazyClock.controller.page', []);

controller.controller('myLazyClock.controller.page', ['$rootScope', 'GApi',
    function homeCtl($rootScope, GApi) {
    	$rootScope.alarmClocks = {}
        GApi.executeAuth('myLazyClock', 'alarmClock.list').then(function(resp) {
        		if (resp.items != undefined)
                	$rootScope.alarmClocks = resp.items;
            });
    }
]);
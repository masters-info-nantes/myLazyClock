var controller = angular.module('myLazyClock.controller.alarmClock.item.view.calendarlist', []);

controller.controller('myLazyClock.controller.alarmClock.item.view.calendarlist', ['$scope', 'GApi', '$stateParams',
    function homeCtl($scope, GApi, $stateParams) {
    	$scope.calendars = {}
    	GApi.executeAuth('myLazyClock', 'calendar.list', {'alarmClockId': $stateParams.id}).then( function(resp) {
    		if (resp.items != undefined)
               	$scope.calendars = resp.items;
        });
        
    }
]);
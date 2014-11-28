var controller = angular.module('myLazyClock.controller.alarmClock.item.view.calendarlist', []);

controller.controller('myLazyClock.controller.alarmClock.item.view.calendarlist', ['$scope', 'GApi', '$stateParams', 'TRAVELS_MODE',
    function homeCtl($scope, GApi, $stateParams, TRAVELS_MODE) {
    	$scope.calendars = {}
    	GApi.executeAuth('myLazyClock', 'calendar.list', {'alarmClockId': $stateParams.id}).then( function(resp) {
    		if (resp.items != undefined) {
               	$scope.calendars = resp.items;
               	for(var i= 0; i < $scope.calendars.length; i++){
               		angular.forEach(TRAVELS_MODE, function(value) {
                	   if (value.id == $scope.calendars[i]['travelMode'])
                		    $scope.calendars[i]['travelMode'] = value.name;
					         });
               	}
           }
        }); 
    }
]);
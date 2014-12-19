var controller = angular.module('myLazyClock.controller.alarmClock.item.view.calendardelete', []);

controller.controller('myLazyClock.controller.alarmClock.item.view.calendardelete', ['$scope', 'GApi', '$stateParams', '$state',
    function homeCtl($scope, GApi, $stateParams, $state) {

        GApi.executeAuth('myLazyClock', 'calendar.item', {'calendarId': $stateParams.calendar, 'alarmClockId': $stateParams.id}).then( function(resp) {
                $scope.calendar = resp;
        });

        $scope.submitDelete = function() {
        	GApi.executeAuth('myLazyClock', 'calendar.delete', {'calendarId': $stateParams.calendar, 'alarmClockId': $stateParams.id}).then( function(resp) {
                $state.go('webapp.alarmClockItem.view.calendarlist');
        	});
        }
    }
]);
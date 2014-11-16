var controller = angular.module('myLazyClock.controller.alarmClock.item.view', []);

controller.controller('myLazyClock.controller.alarmClock.item.view', ['$scope', 'GApi', '$stateParams',
    function homeCtl($scope, GApi, $stateParams) {
        GApi.executeAuth('calendar', 'calendarList.list').then( function(resp) {
               $scope.googleCalendars = resp.items;
            });
        
    }
]);
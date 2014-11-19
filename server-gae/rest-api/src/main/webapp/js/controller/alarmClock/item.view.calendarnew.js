var controller = angular.module('myLazyClock.controller.alarmClock.item.view.calendarnew', []);

controller.controller('myLazyClock.controller.alarmClock.item.view.calendarnew', ['$scope', 'GApi', '$stateParams', 'TRAVELS_MODE',
    function homeCtl($scope, GApi, $stateParams, TRAVELS_MODE) {
        $scope.travelsMode = TRAVELS_MODE;
    	$scope.newCalendar = {}

        $scope.temp = {};
        $scope.tab = 'google';
        
        $scope.onTabSelect = function(tabName) {
            $scope.tab = tabName;
        }

        GApi.executeAuth('calendar', 'calendarList.list').then( function(resp) {
               $scope.googleCalendars = resp.items;
        });

        $scope.submitAdd = function() {
            $scope.newCalendar.travelMode = $scope.temp.travelMode.id;
            if($scope.tab == 'google') {
                $scope.newCalendar.name = $scope.temp.google.summary;
                $scope.newCalendar.param = $scope.temp.google.id;
                $scope.newCalendar.calendarType = 'GOOGLE_CALENDAR';
            }
            if($scope.tab == 'ics') {
                $scope.newCalendar.name = $scope.temp.ics.name;
                $scope.newCalendar.param = $scope.temp.ics.param;
                $scope.newCalendar.calendarType = 'ICS_FILE';
            }
            $scope.newCalendar.alarmClockId = $stateParams.id
            GApi.executeAuth('myLazyClock', 'calendar.add',  $scope.newCalendar).then( function(resp) {
                console.log(resp);
            });
        }
        
    }
]);
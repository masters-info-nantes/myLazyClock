var controller = angular.module('myLazyClock.controller.alarmClock.item.view.calendarnew', []);

controller.controller('myLazyClock.controller.alarmClock.item.view.calendarnew', ['$scope', '$state', 'GApi', '$stateParams', 'TRAVELS_MODE', 'GAuth',
    function homeCtl($scope, $state, GApi, $stateParams, TRAVELS_MODE, GAuth) {
        $scope.travelsMode = TRAVELS_MODE;
    	$scope.newCalendar = {}

        $scope.temp = {};
        $scope.tab = 'google';

        $scope.googleOfflineOk = false;

        GApi.executeAuth('myLazyClock', 'myLazyClockUser.get').then( function(resp) {
            console.log(resp);
            $scope.googleOfflineOk = false;
        });

        $scope.offlineGoogle = function() {
            GAuth.offline().then( function(code){
                console.log(code);
                GApi.executeAuth('myLazyClock', 'myLazyClockUser.link', {code : code}).then( function(resp) {
                    console.log(resp);
                    $scope.googleOfflineOk = true;
                });
            });
        }
        
        $scope.onTabSelect = function(tabName) {
            $scope.tab = tabName;
        }

        GApi.executeAuth('calendar', 'calendarList.list').then( function(resp) {
               $scope.googleCalendars = resp.items;
        });

        GApi.execute('myLazyClock', 'edt.ufr.list').then( function(resp) {
               $scope.edtUfr = resp.items;
        });

        $scope.edtUpdateGroups = function() {
            GApi.execute('myLazyClock', 'edt.groups.list', {ufr : $scope.temp.edt.ufr.id}).then( function(resp) {
               $scope.edtGroups = resp.items;
            });
        }

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
            if($scope.tab == 'edt') {
                $scope.newCalendar.name = $scope.temp.edt.ufr.name+' - '+$scope.temp.edt.group.name;
                $scope.newCalendar.param = $scope.temp.edt.group.id;
                $scope.newCalendar.calendarType = 'EDT';
            }
            $scope.newCalendar.alarmClockId = $stateParams.id
            GApi.executeAuth('myLazyClock', 'calendar.add',  $scope.newCalendar).then( function(resp) {
                console.log(resp);
                $state.go('webapp.alarmClockItem.view.calendarlist');
            });
        }
        
    }
]);
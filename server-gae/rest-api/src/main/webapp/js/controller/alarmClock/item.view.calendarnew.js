var controller = angular.module('myLazyClock.controller.alarmClock.item.view.calendarnew', []);

controller.controller('myLazyClock.controller.alarmClock.item.view.calendarnew', ['$scope', '$state', 'GApi', '$stateParams', 'TRAVELS_MODE', 'GAuth',
    function homeCtl($scope, $state, GApi, $stateParams, TRAVELS_MODE, GAuth) {
        $scope.travelsMode = TRAVELS_MODE;
    	$scope.newCalendar = {}

        $scope.temp = {};
        $scope.tab = 'google';


        GApi.executeAuth('myLazyClock', 'myLazyClockUser.get').then( function(resp) {
            if(resp.valid) {
                $scope.googleOfflineOk = true;
                $scope.googleOfflineKO = false;
            } else {
                $scope.googleOfflineKO = true;
            }
        });

        $scope.offlineGoogle = function() {
            GAuth.offline().then( function(code){
                console.log(code);
                GApi.executeAuth('myLazyClock', 'myLazyClockUser.link', {code : code}).then( function(resp) {
                    if(resp.valid) {
                        $scope.googleOfflineOk = true;
                        $scope.googleOfflineKO = false;
                    } else {
                        $scope.googleOfflineKO = true;
                    }
                });
            });
        }
<<<<<<< HEAD

        $scope.formOk = false;

        $scope.updateForm = function() {
            if($scope.tab == 'google') {
                if($scope.addCalendar.googleParam.$valid)
                    $scope.formOk = true;
                else
                    $scope.formOk = false;
            }
            if($scope.tab == 'ics') {
                if($scope.addCalendar.icsName.$valid && $scope.addCalendar.icsParam.$valid)
                    $scope.formOk = true;
                else
                    $scope.formOk = false;
            }
            if($scope.tab == 'edt') {
                if($scope.addCalendar.edtGoup.$valid && $scope.addCalendar.edtUfr.$valid)
                    $scope.formOk = true;
                else
                    $scope.formOk = false;
            }
            if($scope.formOk) {
                if($scope.addCalendar.travelMode.$valid && $scope.addCalendar.defaultEventLocation.$valid)
                    $scope.formOk = true;
                else
                    $scope.formOk = false;
            }
        }

        $scope.locationOK = false;

        $scope.$on("locationAutocompleted", function(event, location) {
            $scope.locationOK = true;
        })

        $scope.editLocation = function() {
            $scope.locationOK = false;
        }
=======
>>>>>>> FETCH_HEAD
        
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
            $scope.edtGroups = null;
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
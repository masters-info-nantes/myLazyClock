var controller = angular.module('myLazyClock.controller.alarmClock.item.view.calendaredit', []);

controller.controller('myLazyClock.controller.alarmClock.item.view.calendaredit', ['$scope', 'GApi', '$stateParams', '$state', 'TRAVELS_MODE',
    function homeCtl($scope, GApi, $stateParams, $state, TRAVELS_MODE) {

        $scope.travelsMode = TRAVELS_MODE;

        GApi.executeAuth('myLazyClock', 'calendar.item', {'calendarId': $stateParams.calendar, 'alarmClockId':$stateParams.id}).then( function(resp) {
                $scope.calendar = resp;
                angular.forEach($scope.travelsMode, function(value) {
                    if (value.id == $scope.calendar.travelMode) {
                        $scope.calendar.travelMode = value;
                    }
                        
                });
        });

        $scope.locationOK = true;

        $scope.$on("locationAutocompleted", function(event, location) {
            $scope.locationOK = true;
        })

        $scope.editLocation = function() {
            $scope.locationOK = false;
        }

        $scope.submitEdit = function() {
            console.log($scope.calendar.travelMode.id);
            $scope.calendar.travelMode = $scope.calendar.travelMode.id;
            var calendar = angular.copy($scope.calendar);
            calendar.calendarId =  $stateParams.calendar;
            calendar.alarmClockId = $stateParams.id;
            GApi.executeAuth('myLazyClock', 'calendar.update', calendar).then( function(resp) {
                $state.go('webapp.alarmClockItem.view.calendarlist');
            });

        }
    }
]);
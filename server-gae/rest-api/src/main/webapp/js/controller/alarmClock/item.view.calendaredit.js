var controller = angular.module('myLazyClock.controller.alarmClock.item.view.calendaredit', []);

controller.controller('myLazyClock.controller.alarmClock.item.view.calendaredit', ['$scope', 'GApi', '$stateParams', '$state', 'TRAVELS_MODE',
    function homeCtl($scope, GApi, $stateParams, $state, TRAVELS_MODE) {

        $scope.travelsMode = TRAVELS_MODE;

        GApi.executeAuth('myLazyClock', 'calendar.item', {'calendarId': $stateParams.calendar}).then( function(resp) {
                $scope.calendar = resp;
                angular.forEach($scope.travelsMode, function(value) {
                    if (value.id == $scope.calendar.travelMode) {
                        $scope.calendar.travelMode = value;
                    }
                        
                });
        });

        $scope.submitEdit = function() {
            $scope.calendar.travelMode = $scope.calendar.travelMode.id;
            GApi.executeAuth('myLazyClock', 'calendar.update', {'calendar': $scope.calendar}).then( function(resp) {
                $state.go('webapp.alarmClockItem.view.calendarlist');
            });

        }
    }
]);
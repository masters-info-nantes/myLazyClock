var controller = angular.module('myLazyClock.controller.signin', []);

controller.controller('myLazyClock.controller.signin', ['$scope', '$localStorage', '$interval', '$state', 'GApi',
    function signinCtl($scope, $localStorage, $interval, $state, GApi) {
        var interval;

    	var goToHome = function(id) {
    		var check = function(id) {
    			GApi.execute('myLazyClock', 'alarmClock.item', {alarmClockId: id}).then( function(resp) {
                        if(resp.user != undefined) {
                            $interval.cancel(interval);
							$state.go('webapp.home');
                        }
					});
    		}
    		check(id);
    		interval = $interval(function() {
				check(id);
			}, 4000);
    	}
    	if($localStorage.alarmClockId == undefined) {
    		GApi.execute('myLazyClock', 'alarmClock.generate').then( function(resp) {
                $scope.alarmClockId = resp.id;
                $localStorage.alarmClockId = resp.id;
                goToHome($scope.alarmClockId);
            });
    	} else {
    		$scope.alarmClockId = $localStorage.alarmClockId;
    		goToHome($localStorage.alarmClockId);
    	}
        
    }
])
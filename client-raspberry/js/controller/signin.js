var controller = angular.module('myLazyClock.controller.signin', []);

controller.controller('myLazyClock.controller.signin', ['$scope', '$localStorage', '$interval', '$state', 'GApi',
    function signinCtl($scope, $localStorage, $interval, $state, GApi) {
    	var goToHome = function(id) {
    		var check = function(id) {
    			GApi.get('item', {alarmClockId: id}, function(resp) {
						if(resp.user != undefined)
							$state.go('webapp.home');
					});
    		}
    		check(id);
    		$interval(function() {
				check(id);
			}, 4000);
    	}
    	if($localStorage.alarmClockId == undefined) {
    		GApi.get('generate', function(resp) {
                $scope.alarmClockId = resp.id;
                $scope.$apply($scope.alarmClockId);
                $localStorage.alarmClockId = resp.id;
                goToHome($scope.alarmClockId);
            });
    	} else {
    		$scope.alarmClockId = $localStorage.alarmClockId;
    		goToHome($localStorage.alarmClockId);
    	}
        
    }
])
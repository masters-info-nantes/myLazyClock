var controller = angular.module('myLazyClock.controller.home', []);

controller.controller('myLazyClock.controller.home', ['$rootScope', '$scope', '$localStorage', 'GApi', 'ngAudio', '$interval',
    function homeCtl($rootScope, $scope, $localStorage, GApi, ngAudio, $interval) {
    	$scope.sound = ngAudio.load("sounds/ring.mp3");
    	$scope.sound.loop = true;
    	$scope.alarmClockEvents = [];

    	var secondsSinceLastReload = 0;
		var getAlarmClockEvent = function() {
			console.log('reload after : '+secondsSinceLastReload)
			secondsSinceLastReload = 0;
    		GApi.execute('myLazyClock', 'alarmClock.item', {alarmClockId: $localStorage.alarmClockId}).then(function(resp) {
				$rootScope.alarmClock = resp;
				console.log($rootScope.alarmClock);
				GApi.execute('myLazyClock', 'clockevent.list', {alarmClockId: $localStorage.alarmClockId}).then(function(resp) {
					$scope.alarmClockEvents = resp.items;
				});
			});
			
    	}

    	var getAlarmClockEventLoop = function() {
    		var update = function() {
    			var now = new Date().getTime();
    			if(secondsSinceLastReload == 240) // 4heures en minutes
    				getAlarmClockEvent();
    			if($scope.clockEvent != undefined) {
    				if ($scope.clockEvent.wakeUpDate-now > ($scope.clockEvent.travelDuration*1000)-60000 && $scope.clockEvent.wakeUpDate-now < ($scope.clockEvent.travelDuration*1000)+60000) // 
    					getAlarmClockEvent();
    			}
    			secondsSinceLastReload++;
    		}
    		update();
    		$interval(function() {
				update();
			}, 60000);
    	}

    	var ringCtl = function() {
    		var updateRing = function() {
    			var nextEvent;
    			var now = new Date().getTime();
    			for(var i= 0; i < $scope.alarmClockEvents.length; i++){
    				if($scope.alarmClockEvents[i]['wakeUpDate'] == undefined )
    					$scope.alarmClockEvents[i]['wakeUpDate'] = $scope.alarmClockEvents[i]['beginDateTime']-$scope.alarmClockEvents[i]['travelDuration']*1000-$rootScope.alarmClock.preparationTime*1000;
    				if(i == 0)
    					nextEvent = $scope.alarmClockEvents[i];
    				else {
    					if($scope.alarmClockEvents[i]['wakeUpDate'] < nextEvent.wakeUpDate)
    						nextEvent = $scope.alarmClockEvents[i];
    				}
    				if (($scope.alarmClockEvents[i]['wakeUpDate']-now) <= -20000) {
                		$scope.sound.stop();
                		if (i > -1) {
                            $scope.alarmClockEvents.splice(i--, 1);
                        }
                	}
                };
                if (nextEvent != undefined) {
                	
                	$scope.clockEvent = nextEvent;
                	//console.log($scope.clockEvent.wakeUpDate-now);
                	if (($scope.clockEvent.wakeUpDate-now) < 0 && ($scope.clockEvent.wakeUpDate-now) > -20000) {
                		$scope.sound.play();
                	}
                	
                }
    		}
    		updateRing();
    		$interval(function() {
				updateRing();
			}, 1000);
    	}

    	getAlarmClockEvent();
    	getAlarmClockEventLoop();
    	ringCtl();
    }
]);
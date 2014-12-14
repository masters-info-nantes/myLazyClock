var controller = angular.module('myLazyClock.controller.home', []);

controller.controller('myLazyClock.controller.home', ['$rootScope', '$scope', '$state', '$localStorage', 'GApi', 'ngAudio', '$interval', 'hotkeys', '$rootScope', '$localStorage',
    function homeCtl($rootScope, $scope, $state, $localStorage, GApi, ngAudio, $interval, hotkeys, $rootScope, $localStorage) {
    	$scope.sound = ngAudio.load("sounds/ring.mp3");
    	$scope.sound.loop = true;
        $scope.sound.volume = 1;
        $rootScope.volumeUI = 4;
    	$scope.alarmClockEvents = [];
        $scope.soundStop = false;

        var interval1;
        var interval2;

        $scope.isRaspClient = false;
        if(navigator.userAgent == 'raspmylazyclock')
            $scope.isRaspClient = true;

        $scope.stop = function() {
            $scope.soundStop = true;
            $scope.sound.stop();
        }

        hotkeys.add({
            combo: 's',
            description: 'stop ring',
            callback: $scope.stop
        });

        $scope.reload = function() {
            getAlarmClockEvent();
        }

        hotkeys.add({
            combo: 'r',
            description: 'reload all',
            callback: $scope.reload
        });

        $scope.volumeUp = function() {
            if ($localStorage.volume < 4) {
                setVolume($rootScope.volumeUI+1);
            }
        }

        hotkeys.add({
            combo: 'p',
            description: 'up volume',
            callback: $scope.volumeUp
        });

        $scope.volumeDown = function() {
            if ($localStorage.volume > 0) {
                setVolume($rootScope.volumeUI-1);
            }
        }

        hotkeys.add({
            combo: 'm',
            description: 'down volume',
            callback: $scope.volumeDown
        });
        

        if ($localStorage.volume == undefined)
            setVolume(4);
        else
            setVolume($localStorage.volume);

        function setVolume(value) {
            $scope.sound.volume = (value*0.25).toFixed(2);
            $rootScope.volumeUI = value;
            $localStorage.volume = value;
        }

    	var secondsSinceLastReload = 0;
		var getAlarmClockEvent = function() {
			console.log('reload after : '+secondsSinceLastReload)
			secondsSinceLastReload = 0;
    		GApi.execute('myLazyClock', 'alarmClock.item', {alarmClockId: $localStorage.alarmClockId}).then(function(resp) {
				$rootScope.alarmClock = resp;
				if(resp.user == undefined) {
                    $interval.cancel(interval1);
                    $interval.cancel(interval2);
                    $state.go('webapp.signin');
                }
                $scope.sound = ngAudio.load("sounds/"+resp.ringtone);
                setVolume($localStorage.volume);
				GApi.execute('myLazyClock', 'clockevent.list', {alarmClockId: $localStorage.alarmClockId}).then(function(resp) {
					$scope.alarmClockEvents = resp.items;
                    $scope.error = undefined;
				}, function(resp) {
                    $scope.error = resp;
                });
			}, function() {
                $interval.cancel(interval1);
                $interval.cancel(interval2);
                $state.go('webapp.signin');
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
    		interval1 = $interval(function() {
				update();
			}, 60000);
    	}

    	var ringCtl = function() {
    		var updateRing = function() {
    			var nextEvent;
    			var now = new Date().getTime();
                if($scope.alarmClockEvents != undefined) {
    			for(var i= 0; i < $scope.alarmClockEvents.length; i++){
    				if($scope.alarmClockEvents[i]['wakeUpDate'] == undefined ) {
    					$scope.alarmClockEvents[i]['wakeUpDate'] = $scope.alarmClockEvents[i]['beginDateTime']-$scope.alarmClockEvents[i]['travelDuration']*1000-$rootScope.alarmClock.preparationTime*1000;
                    }
    				if(i == 0)
    					nextEvent = $scope.alarmClockEvents[i];
    				else {
    					if($scope.alarmClockEvents[i]['wakeUpDate'] < nextEvent.wakeUpDate)
    						nextEvent = $scope.alarmClockEvents[i];
    				}
    				if (($scope.alarmClockEvents[i]['wakeUpDate']-now) <= -600000 || (($scope.alarmClockEvents[i]['wakeUpDate']-now) < -1000 && ($scope.alarmClockEvents[i]['wakeUpDate']-now) >= -600000) && $scope.soundStop) {
                		$scope.sound.stop();
                		if (i > -1) {
                            $scope.alarmClockEvents.splice(i--, 1);
                        }
                	}
                };
                }
                $scope.clockEvent = nextEvent;
                if (nextEvent != undefined) {
                	
                    if (($scope.clockEvent.wakeUpDate-now) < 0 && ($scope.clockEvent.wakeUpDate-now) >= -1000) {
                        $scope.sound.play();
                        $scope.soundStop = false;
                    }
                	if (($scope.clockEvent.wakeUpDate-now) < -1000 && ($scope.clockEvent.wakeUpDate-now) > -600000) {
                		$scope.sound.play();
                	}
                	
                }
    		}
    		updateRing();
    		interval2 = $interval(function() {
				updateRing();
			}, 1000);
    	}

    	getAlarmClockEvent();
    	getAlarmClockEventLoop();
    	ringCtl();
    }
]);
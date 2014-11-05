

var controller = angular.module('myLazyClock.controller.alarmClock.add', []);

controller.controller('myLazyClock.controller.alarmClock.add', ['$scope', '$rootScope', 'Api',
    function homeCtl($scope, $rootScope, Api) {
    	$scope.submitAdd = function(){
        	Api.post('link',{
          		alarmClockId: $scope.alarmClock.id
        	}, function(resp) {
                console.log("todos api sucessfully called");
                console.log(resp);
                alert(resp.id);
            });
        
    	}
    }
]);
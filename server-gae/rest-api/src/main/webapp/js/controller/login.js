var controller = angular.module('myLazyClock.controller.login', []);

controller.controller('myLazyClock.controller.login', ['$scope', 'GAuth', 'GApi', '$state',
    function clientList($scope, GAuth, GApi, $state) {
    	if(GApi.isLogin()){
    		$state.go('webapp.home');
    	}

        $scope.doLogin = function() {
            GAuth.login().then(function(){
            	$state.go('webapp.home');
            });
        };
    }
])
var controller = angular.module('myLazyClock.controller.login', []);

controller.controller('myLazyClock.controller.login', ['$scope', 'GAuth', '$state',
    function clientList($scope, GAuth, $state) {
    	if(GAuth.isLogin())
        	$state.go('webapp.home');
        
        $scope.doLogin = function() {
            GAuth.login().then(function(){
            	$state.go('webapp.home');
            });
        };
    }
])
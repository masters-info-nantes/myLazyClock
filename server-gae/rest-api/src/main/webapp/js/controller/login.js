var controller = angular.module('myLazyClock.controller.login', []);

controller.controller('myLazyClock.controller.login', ['$scope', 'GAuth', '$state',
    function clientList($scope, GAuth, $state) {
        
        $scope.doLogin = function() {
            GAuth.signin(function(){
            	$state.go('webapp.home');
            });
        };
    }
])
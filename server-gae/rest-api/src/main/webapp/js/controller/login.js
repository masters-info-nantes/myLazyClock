var controller = angular.module('myLazyClock.controller.login', []);

controller.controller('myLazyClock.controller.login', ['$scope', 'Auth', '$state',
    function clientList($scope, Auth, $state) {
        
        $scope.doLogin = function() {
            Auth.signin(function(){
            	$state.go('webapp.home');
            });
        };
    }
])
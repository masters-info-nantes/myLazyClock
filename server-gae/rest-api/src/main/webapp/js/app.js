var app = angular.module('myLazyClock', [

    'ui.bootstrap',
    'ui.router',

    'myLazyClock.router',
    'myLazyClock.controller',
    'myLazyClock.auth',
    'myLazyClock.api',

]);

app.run(['$window', '$rootScope', 'Auth',
    function($window, $rootScope, Auth) {
        //$rootScope.LOAD_GAE_MODULE = false;
        $window.init= function() {
            console.log("$window.init called");
            //$rootScope.LOAD_GAE_MODULE = true;
            Auth.load();
        };

    }
]);
var app = angular.module('myLazyClock', [

    'ui.router',
    'angular-google-gapi',
    'ngStorage',
    'ngAudio',
    'cfp.hotkeys',

    'myLazyClock.router',
    'myLazyClock.controller',

]);

app.run(['GApi', '$state', '$rootScope', '$http', '$interval',
    function(GApi, $state, $rootScope, $http, $interval) {

        //var BASE = 'http://localhost:8080/_ah/api';
        var BASE = 'https://mylazyclock.appspot.com/_ah/api';

        var init = false;

        $rootScope.online = false;

        var isOnline = function() {
          $http.get('https://api.github.com/').
            success(function(data, status, headers, config) {
              $rootScope.online = true;
              if(!init) {
                init = true;
                GApi.load('myLazyClock','v1',BASE);
              }
            }).
            error(function(data, status, headers, config) {
              $rootScope.online = false;
            });
        }

        isOnline();
        $interval(function() {
          isOnline();
        }, 4000);

    }
]);
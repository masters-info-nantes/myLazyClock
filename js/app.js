var app = angular.module('myLazyClock', [

    'ui.router',
    'angular-google-gapi',
    'ngStorage',
    'ngAudio',
    'cfp.hotkeys',

    'myLazyClock.router',
    'myLazyClock.controller',

]);

app.run(['GApi', '$state', '$rootScope', '$window',
    function(GApi, $state, $rootScope, $window) {

        //var BASE = 'http://localhost:8080/_ah/api';
        var BASE = 'https://mylazyclock.appspot.com/_ah/api';

        GApi.load('myLazyClock','v1',BASE);

        $rootScope.online = window.navigator.onLine;
      $window.addEventListener("offline", function () {
        $rootScope.$apply(function() {
          $rootScope.online = false;
        });
      }, false);
      $window.addEventListener("online", function () {
        $rootScope.$apply(function() {
          $rootScope.online = true;
        });
      }, false);

    }
]);
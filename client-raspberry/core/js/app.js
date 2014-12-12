var app = angular.module('myLazyClock', [

    'ui.router',
    'angular-google-gapi',
    'ngStorage',
    'ngAudio',
    'cfp.hotkeys',

    'myLazyClock.router',
    'myLazyClock.controller',

]);

app.run(['GApi', '$state',
    function(GApi, $state) {

        //var BASE = 'http://localhost:8080/_ah/api';
        var BASE = 'https://mylazyclock.appspot.com/_ah/api';

        GApi.load('myLazyClock','v1',BASE);

    }
]);
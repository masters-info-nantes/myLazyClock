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

        var BASE = 'https://mylazyclock.appspot.com/_ah/api';
        if(window.location.hostname == 'localhost') {
            if(window.location.port == '8080') {
                BASE = 'http://localhost:8080/_ah/api';
            } else {
                BASE = 'http://localhost:8080/_ah/api';
            } 
        } else {
            BASE = 'https://mylazyclock.appspot.com/_ah/api';
        }
        var BASE = 'https://mylazyclock.appspot.com/_ah/api';

        GApi.load('myLazyClock','v1',BASE);

    }
]);
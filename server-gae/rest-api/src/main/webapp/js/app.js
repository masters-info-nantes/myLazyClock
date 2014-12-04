var app = angular.module('myLazyClock', [

    'ui.bootstrap',
    'templates.ui.bootstrap',
    'ui.router',
    'angular-google-gapi',

    'myLazyClock.router',
    'myLazyClock.controller',

]);

app.constant('TRAVELS_MODE',[
    {'name' : 'A pied', 'id' : 'WALKING'},
    {'name' : 'En voiture', 'id' : 'DRIVING'},
    {'name' : 'En vélo', 'id' : 'BICYCLING'},
    {'name' : 'En Transport', 'id' : 'TRANSIT'}
]);

app.constant('PREPARATION_TIMES',[
    {'name' : '5 minutes', 'time' : '300'},
    {'name' : '15 minutes', 'time' : '900'},
    {'name' : '20 minutes', 'time' : '1200'}
]);

app.config(function ($provide) {
  $provide.decorator('$uiViewScroll', function ($delegate) {
    return function (uiViewElement) {
      window.scrollTo(0, (top - 30));
      // Or some other custom behaviour...
    }; 
  });
});

app.run(['GAuth', 'GApi', '$state',
    function(GAuth, GApi, $state) {

        var CLIENT;
        var BASE;
        if(window.location.hostname == 'localhost') {
            if(window.location.port == '8080') {
                CLIENT = '1072024627812-kgv1uou2btdphtvb2l2bbh14n6u2n2mg.apps.googleusercontent.com';
                BASE = '//localhost:8080/_ah/api';
            } else {
                CLIENT = '1072024627812-69lrpihiunbo6rrpqpnkho7djdl5fu74.apps.googleusercontent.com';
                BASE = '//localhost:8080/_ah/api';
            } 
        } else {
            CLIENT = '1072024627812-oh4jdt3mo6rihojkt480tqfsja2706b4.apps.googleusercontent.com';
            BASE = 'https://mylazyclock.appspot.com/_ah/api';
        }

        GApi.load('myLazyClock','v1',BASE);
        GApi.load('calendar','v3');
        GAuth.setClient(CLIENT);
        GAuth.setScopes(['https://www.googleapis.com/auth/userinfo.email','https://www.googleapis.com/auth/calendar.readonly']);
        GAuth.checkAuth().then(
            function () {
                if($state.includes('login'))
                    $state.go('webapp.home');
            },
            function() {
                $state.go('login');
            }
        );
    }
]);
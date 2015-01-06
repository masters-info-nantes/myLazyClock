var app = angular.module('myLazyClock', [

    'ui.bootstrap',
    'templates.ui.bootstrap',
    'ui.router',
    'angular-google-gapi',
    'locompleter',

    'myLazyClock.router',
    'myLazyClock.controller',

]);

app.constant('TRAVELS_MODE',[
    {'name' : 'A pied', 'id' : 'WALKING'},
    {'name' : 'En voiture', 'id' : 'DRIVING'},
    {'name' : 'En vélo', 'id' : 'BICYCLING'},
    {'name' : 'En Transport en commun', 'id' : 'TRANSIT'}
]);

app.constant('RINGTONES',[
    {'name' : 'Vieux téléphone', 'file' : 'ring'},
    {'name' : 'Tetris', 'file' : 'tetris'}
]);

app.constant('PREPARATION_TIMES',[
    {'name' : '5 minutes', 'time' : '300'},
    {'name' : '10 minutes', 'time' : '600'},
    {'name' : '15 minutes', 'time' : '900'},
    {'name' : '20 minutes', 'time' : '1200'},
    {'name' : '25 minutes', 'time' : '1500'},
    {'name' : '30 minutes', 'time' : '1800'},
    {'name' : '35 minutes', 'time' : '2100'},
    {'name' : '40 minutes', 'time' : '2400'},
    {'name' : '45 minutes', 'time' : '2700'},
    {'name' : '50 minutes', 'time' : '3000'},
    {'name' : '55 minutes', 'time' : '3300'},
    {'name' : '1 heure', 'time' : '3600'},
    {'name' : '1 heure et 5 minutes', 'time' : '3900'},
    {'name' : '1 heure et 10 minutes', 'time' : '4200'},
    {'name' : '1 heure et 15 minutes', 'time' : '4500'},
    {'name' : '1 heure et 20 minutes', 'time' : '4800'},
    {'name' : '1 heure et 25 minutes', 'time' : '5100'},
    {'name' : '1 heure et 30 minutes', 'time' : '5400'},
    {'name' : '1 heure et 35 minutes', 'time' : '5470'},
    {'name' : '1 heure et 40 minutes', 'time' : '6000'},
    {'name' : '1 heure et 45 minutes', 'time' : '6300'},
    {'name' : '1 heure et 50 minutes', 'time' : '6600'},
    {'name' : '1 heure et 55 minutes', 'time' : '6900'},
    {'name' : '2 heures', 'time' : '7200'}
]);

app.config(function ($provide) {
  $provide.decorator('$uiViewScroll', function ($delegate) {
    return function (uiViewElement) {
      window.scrollTo(0, (top - 30));
      // Or some other custom behaviour...
    }; 
  });
});

app.run(['GAuth', 'GApi', '$state', '$rootScope',
    function(GAuth, GApi, $state, $rootScope) {

        var CLIENT;
        var BASE;
        if(window.location.hostname == 'localhost') {
                CLIENT = '1072024627812-kgv1uou2btdphtvb2l2bbh14n6u2n2mg.apps.googleusercontent.com';
                BASE = '//localhost:8080/_ah/api';
        } else {
            CLIENT = '1072024627812-oh4jdt3mo6rihojkt480tqfsja2706b4.apps.googleusercontent.com';
            BASE = 'https://mylazyclock.appspot.com/_ah/api';
        }

        GApi.load('myLazyClock','v1',BASE);
        GApi.load('calendar','v3');
        GAuth.setClient(CLIENT);
        GAuth.setScopes('https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/calendar.readonly');
        GAuth.checkAuth().then(
            function () {
                if($state.includes('login'))
                    $state.go('webapp.home');
            },
            function() {
                $state.go('login');
            }
        );

        $rootScope.logout = function() {
            GAuth.logout().then(
            function () {
                $state.go('login');
            });
        };
    }
]);
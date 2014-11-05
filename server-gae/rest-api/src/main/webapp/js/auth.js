var auth = angular.module('myLazyClock.auth', []);

auth.factory('Auth', ['$rootScope', '$state', '$window', 'Api',
    function($rootScope, $state, $window, Api){

        var CLIENT_ID = '1072024627812-oh4jdt3mo6rihojkt480tqfsja2706b4.apps.googleusercontent.com';
        var CLIENT_ID_DEV_WEB = '1072024627812-69lrpihiunbo6rrpqpnkho7djdl5fu74.apps.googleusercontent.com';
        var CLIENT_ID_DEV = '1072024627812-kgv1uou2btdphtvb2l2bbh14n6u2n2mg.apps.googleusercontent.com';
        var SCOPES = ['https://www.googleapis.com/auth/userinfo.email'];
        var RESPONSE_TYPE = 'token id_token';

        function signin(mode, authorizeCallback) {
            gapi.auth.authorize({client_id: CLIENT_ID,
                    scope: SCOPES, immediate: mode, response_type : RESPONSE_TYPE},
                authorizeCallback);
        }

        function getUser() {
            var request =
                gapi.client.oauth2.userinfo.get().execute(function(resp) {
                    if (!resp.code) {
                        $rootScope.user = {};
                        $rootScope.user.email = resp.email;
                        $rootScope.user.picture = resp.picture;
                        $rootScope.user.id = resp.id;
                        $rootScope.user.name = resp.name;
                        $rootScope.user.link = resp.link;
                        $rootScope.$apply($rootScope.user);
                        $rootScope.LOAD_GAE_MODULE = true;
                        $rootScope.$apply($rootScope.LOAD_GAE_MODULE);
                        Api.load();
                    }
                    else {
                        $state.go('webapp.login');
                    }
                });
        }

        return {

            load: function(){
                gapi.client.load('oauth2', 'v2', function() {
                    console.log("oauth load");
                    signin(true, getUser);
                });

            },

            signin: function(calback){                
                signin(false, function() {
                    getUser();
                    calback();
                });
            },


        }

    }]);
var api = angular.module('myLazyClock.api', []);

auth.factory('Api', ['$rootScope', '$state', '$window',
    function($rootScope, $state, $window){
        var BASE_URL;
        if ($window.location.port == '')
            BASE_URL = 'https://'+$window.location.hostname+'/_ah/api';
        else
            BASE_URL = 'http://'+$window.location.hostname+':'+$window.location.port+'/_ah/api';
        var LOAD_API = false;

        var observerCallbacks = [];
        var observerCallbacksname = [];
        var observerCallbacksPostarray = [];

        function registerObserverCallback(name, postarray, callback){
            observerCallbacksname.push(name);
            observerCallbacksPostarray.push(postarray);
            observerCallbacks.push(callback);
        };

        function load() {
            gapi.client.load('myLazyClock', 'v1', function() {
                console.log("myLazyClock api loaded");
                LOAD_API = true;
                var i = 0;
                angular.forEach(observerCallbacks, function(callback){
                    gapi.client.myLazyClock[observerCallbacksname[i]](observerCallbacksPostarray[i++]).execute(callback);
                });
            }, BASE_URL);
        }

        function api(apiname, postarray, apifunction) {
            if (LOAD_API) {
                gapi.client.myLazyClock[apiname](postarray).execute(apifunction);
            }
            else
                registerObserverCallback(apiname, postarray, apifunction);
        }

        return {

            load: function(){
                load();
            },

            post: function(apiname, postarray, apifunction){                
                api(apiname, postarray, apifunction);
            },

            get: function(apiname, apifunction){                
                api(apiname, null, apifunction);
            },


        }

    }]);
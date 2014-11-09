var router = angular.module('myLazyClock.router', []);

router
    .config(['$urlRouterProvider',
        function($urlRouterProvider) {

            $urlRouterProvider.otherwise("/");

        }]);

router
    .config(['$stateProvider',
        function($stateProvider) {

            $stateProvider

                .state('webapp', {
                    abstract: true,
                    url: '',
                    templateUrl: 'partials/page.html',
                })

                .state('webapp.signin', {
                    url :'/',
                    views :  {
                        '': {
                            templateUrl: 'partials/signin.html',
                            controller: 'myLazyClock.controller.signin',
                        },
                    },
                })

                .state('webapp.home', {
                    url :'/',
                    views :  {
                        '': {
                            controller: 'myLazyClock.controller.home',
                            templateUrl: 'partials/home.html',
                        },
                    },
                })

                
    }])
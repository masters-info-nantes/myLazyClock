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

                .state('webapp.home', {
                    url :'/',
                    views :  {
                        '': {
                            controller: 'myLazyClock.controller.home',
                            templateUrl: 'partials/home.html',
                        },
                    },
                })

                .state('webapp.login', {
                    url :'/',
                    views :  {
                        '': {
                            templateUrl: 'partials/login.html',
                            controller: 'myLazyClock.controller.login',
                        },
                    },
                })

                .state('webapp.alarmClockAdd', {
                    url :'/add',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/add.html',
                            controller: 'myLazyClock.controller.alarmClock.add',
                        },
                    },
                })

                .state('webapp.alarmClockItem', {
                    url :'/alarmClock/{id}',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/item.html',
                            controller: 'myLazyClock.controller.alarmClock.item',
                        },
                    },
                })

                .state('webapp.alarmClockGenerate', {
                    url :'/generate',
                    views :  {
                        '': {
                            controller: 'myLazyClock.controller.alarmClock.generate',
                        },
                    },
                })
    }])
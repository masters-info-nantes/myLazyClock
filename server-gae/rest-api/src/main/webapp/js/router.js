var router = angular.module('myLazyClock.router', []);

router
    .config(['$urlRouterProvider',
        function($urlRouterProvider) {

            $urlRouterProvider.otherwise("/login");

        }]);

router
    .config(['$stateProvider',
        function($stateProvider) {

            $stateProvider

                .state('login', {
                    url :'/login',
                    views :  {
                        '': {
                            templateUrl: 'partials/login.html',
                            controller: 'myLazyClock.controller.login',
                        },
                    },
                })

                .state('webapp', {
                    abstract: true,
                    url: '',
                    templateUrl: 'partials/page.html',
                    controller: 'myLazyClock.controller.page',
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
                    abstract: true,
                    url :'/alarmClock/{id}',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/item.html',
                            controller: 'myLazyClock.controller.alarmClock.item',
                        },
                    },
                })

                .state('webapp.alarmClockItem.view', {
                    abstract: true,
                    url :'',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/itemview.html',
                        },
                    },
                })

                .state('webapp.alarmClockItem.view.calendarlist', {
                    url :'/',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/itemviewcalendarlist.html',
                            controller: 'myLazyClock.controller.alarmClock.item.view.calendarlist',
                        },
                    },
                })

                .state('webapp.alarmClockItem.view.calendarnew', {
                    url :'/new',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/itemviewcalendarnew.html',
                            controller: 'myLazyClock.controller.alarmClock.item.view.calendarnew',
                        },
                    },
                })

                .state('webapp.alarmClockItem.view.calendaredit', {
                    url :'/{calendar}/edit',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/itemviewcalendaredit.html',
                            controller: 'myLazyClock.controller.alarmClock.item.view.calendaredit',
                        },
                    },
                })

                .state('webapp.alarmClockItem.view.calendardelete', {
                    url :'/{calendar}/delete',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/itemviewcalendardelete.html',
                            controller: 'myLazyClock.controller.alarmClock.item.view.calendardelete',
                        },
                    },
                })

                .state('webapp.alarmClockItem.edit', {
                    url :'/edit',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/itemedit.html',
                            controller: 'myLazyClock.controller.alarmClock.item.edit',
                        },
                    },
                })

                .state('webapp.alarmClockItem.delete', {
                    url :'/delete',
                    views :  {
                        '': {
                            templateUrl: 'partials/alarmClock/itemdelete.html',
                            controller: 'myLazyClock.controller.alarmClock.item.delete',
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
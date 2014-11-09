myLazyClock  [![Build Status](https://travis-ci.org/masterALMA2016/myLazyClock.svg?branch=develop)](https://travis-ci.org/masterALMA2016/myLazyClock)
===========

My Lazy Smart Alarm Clock wake up and It's never time.

User Case
---------

I go to sleep without verify my edt, but I forgot the TAN changed schedule and I wasn't see, the first course was canceled.

myLazyClock checks my edt and retards my wake up time and I can sleep more, It checks the route and alerts me, at my wake up, to new schedule of my transport.

Device compatible
-----------------

- Personal computer with a browser HTML5 (Windows, Mac OS, Linux)
- Raspberry


Developer
---------

For Compile the project

```
mvn install -Dmaven.test.skip=true
# or
mvn clean install -Dmaven.test.skip=true
```

For launch the google app engine dev serveur

```
mvn -pl rest-api appengine:devserver
```

For deploy on google app engine

```
mvn -pl rest-api appengine:update
```

Contribute
----------

We use git with [git-flow](http://nvie.com/posts/a-successful-git-branching-model/)

```
git clone https://github.com/masterALMA2016/myLazyClock.git -b develop
```

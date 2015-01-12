myLazyClock  [![Build Status](https://travis-ci.org/masterALMA2016/myLazyClock.svg?branch=develop)](https://travis-ci.org/masterALMA2016/myLazyClock)
===========

My Lazy Smart Alarm Clock wake up and It's never time.

User Case
---------

I go to sleep without verify my edt, but I forgot the TAN changed schedule and I wasn't see, the first course was canceled.

myLazyClock checks all my calendars and retards my wake up time, and I can sleep more, It checks the route and alerts me, at my wake up, to new schedule of my transport.

Device compatibility
--------------------

- Personal computer with a browser HTML5 (Windows, Mac OS, Linux)
- Raspberry Pi


Developer
---------

To compile the project

```
mvn install -Dmaven.test.skip=true
# or
mvn clean install -Dmaven.test.skip=true
```

To launch the google app engine dev serveur

```
mvn -pl rest-api appengine:devserver
```

How To deploy
-------------

Add  profile in ~/.m2/settings.xml, all information of api key are in your [admin console](https://console.developers.google.com) in credential part

```
<settings>
    ...
    <profiles>
        <profile>
            <id>myLazyClockProd</id>
            <properties>
                <api.id>API_Client_Id</api.id>
                <api.secret>API_Client_Secret</api.secret>
            </properties>
        </profile>
    </profiles>
    ...
</settings>
```
And execute this command for recompile the project with good profile, and deploy the project
```
mvn clean install -P myLazyClockProd && mvn -pl rest-api, appengine:update
```

Contribute
----------

We use git with this [git-flow](http://nvie.com/posts/a-successful-git-branching-model/)

Clone the project with this command: 
```
git clone https://github.com/masterALMA2016/myLazyClock.git -b develop
```

Or fork-it

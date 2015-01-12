myLazyClock - Install Raspberry Pi client
===========

Needed before installation
-----------

- a screen with resolution of 320x240 pixels or more

How to use your My Lazy Clock client ?
-----------

For the first run of the alarm clock, you have to follow instruction on the screen.
When you're alarm clock is configure, you could insteract with it with 4 buttons or 4 keys. For the buttons you need to bind it to the following keys :

- R = Refresh manually the alarm clock
- S = Stop alarm / Switch to night mode
- P = increase volume
- M = decrease volume


Easy installation for Raspberry Pi
-----------

You can download our ready-to-use SD card image with MyLazyClock client already install. All necessary drivers for [Adafruit 2.8" TFT screen](https://www.adafruit.com/products/1601) or compatible are installed too. You only need to copy the SD card image on your (micro) SD card, insert it inside your Raspberry Pi, connect screen and ethernet cable then power on it.

SD card could be download here : https://mega.co.nz/#F!foggVYYY!6gtqLY5cT7H0Eg6s8j4SoA

Note : to reproduce this installation type, you have just to follow "Manual installation with Firefox"

Manual installation with Firefox
-----------

- download official Raspbian Image or an existing minimal image like [this](http://sourceforge.net/projects/minibian/)
NOTE : If you choose to start with a minimal image, your alarm clock may be faster.
NOTE 2 : If you choose to use a touchscreen (like Adafruit 2.8" TFT), it's recommanded to start with a proposal image or follow instructions to how install drivers before continue.
- upgrade all your system
```
	apt-get update && apt-get upgrade -y
```
- for minimal installation only : install Xorg and OpenBox and create a new user with a password
NOTE : there is no dependence with OpenBox, so if you want you could use another windows manager.
```
	apt-get install xorg openbox
	useradd -m newusername
	passwd newusername
```
- install : Firefox, lighttpd and audio elements
```
	apt-get install firefox lighttpd alsa-utils mplayer
```
- install unclutter and xdotool (for hide cursor)
```
	apt-get install unclutter xdotool
```
- if you are logined with root account, login with your user account
- for minimal installation only : configuration of OpenBox
```
	mkdir -p ~/.config/openbox/
	cp -fv /etc/xdg/openbox/{autostart,menu.xml,rc.xml} ~/.config/openbox/
```
- for minimal installation only : edit ~/.bash_profile and add at the beginning
```
amixer cset numid=3 1

[[ $(tty) = '/dev/tty1' ]] && startx
```
- optional : for autologin with no display manager (SLiM, GDM, ...), edit /etc/inittab and replace
```
1:2345:respawn:/sbin/getty --noclear 38400 tty1
```
	by
```
1:2345:respawn:/bin/login -f newusername tty1 </dev/tty1 >/dev/tty1 2>&1
```
- optional : to disable screensaver and screen blanking add to ~/.xinitrc
```
xset -dpms
xset s off
```
- add to ~/.config/openbox/autostart (change "320 240" with your screen resolution)
```
unclutter -display :0.0 -idle 1 &
xdotool mousemove 320 240 &
while true;do firefox;done;
```
- for this step you have two possibilies to doing :

**pre-configure firefox profil :**

	- download from https://mega.co.nz/#F!foggVYYY!6gtqLY5cT7H0Eg6s8j4SoA the lastest "XXXX-XX-XX_mozilla-firefox-profile.zip" (where XXXX-XX-XX is the date of the release in YYYY-MM-DD format)
	- extract zip at the root of your newusername with :
	```
	unzip XXXX-XX-XX_mozilla-firefox-profile.zip
	```
	Note: after this you should now have a .mozilla directory in the home folder (you could see it with "ls -a")

**full manual way :**

	- run startx command from Raspberry Pi directy
	- when firefox is started, close X server by running following command through ssh or by changing tty on the Raspberry Pi with Ctrl+Alt+f2 :
	```
	pkill X
	```
	- add some configuration to firefox with editing of ~/.mozilla/firefox/XXXXXXXX.default/prefs.js (where "XXXXXXXX.default" is your profil name, by default generate at the first start of firefox)
	```
user_pref("browser.sessionstore.resume_from_crash", false);
user_pref("browser.startup.homepage", "http://localhost/");
user_pref("browser.tabs.warnOnClose", false);
user_pref("browser.tabs.warnOnCloseOtherTabs", false);
user_pref("browser.tabs.warnOnOpen", false);
user_pref("general.useragent.override", "raspmylazyclock");
	```

- download client from the github and extract it, then copy all content from myLazyClock/client-raspberry/core/ to /var/www/

- now you could reboot and see your own MyLazyClock starting !


Manual installation with Chromium
-----------

- download official Raspbian Image or an existing minimal image like [this](http://sourceforge.net/projects/minibian/)
NOTE : If you choose to start with a minimal image, your alarm clock may be faster.
NOTE 2 : If you choose to use a touchscreen (like Adafruit 2.8" TFT), it's recommanded to start with a proposal image or follow instructions to how install drivers before continue.
- upgrade all your system
```
	apt-get update && apt-get upgrade -y
```
- for minimal installation only : install Xorg and OpenBox and create a new user with a password
NOTE : there is no dependence with OpenBox, so if you want you could use another windows manager.
```
	apt-get install xorg openbox
	useradd -m newusername
	passwd newusername
```
- install : Chromium, lighttpd and audio elements
```
	apt-get install chromium lighttpd alsa-utils mplayer
```
- install unclutter and xdotool (for hide cursor)
```
	apt-get install unclutter xdotool
```
- if you are logined with root account, login with your user account
- for minimal installation only : configuration of OpenBox
```
	mkdir -p ~/.config/openbox/
	cp -fv /etc/xdg/openbox/{autostart,menu.xml,rc.xml} ~/.config/openbox/
```
- for minimal installation only : edit ~/.bash_profile and add at the beginning
```
amixer cset numid=3 1

[[ $(tty) = '/dev/tty1' ]] && startx
```
- optional : for autologin with no display manager (SLiM, GDM, ...), edit /etc/inittab and replace
```
1:2345:respawn:/sbin/getty --noclear 38400 tty1
```
	by
```
1:2345:respawn:/bin/login -f newusername tty1 </dev/tty1 >/dev/tty1 2>&1
```
- optional : to disable screensaver and screen blanking add to ~/.xinitrc
```
xset -dpms
xset s off
```
- add to ~/.config/openbox/autostart (change "320 240" with your screen resolution)
```
unclutter -display :0.0 -idle 1 &
xdotool mousemove 320 240 &
while true;do chromium --disable-session-crashed-bubble --user-agent=raspmylazyclock --kiosk http://localhost;done
```
- download client from the github and extract it, then copy all content from myLazyClock/client-raspberry/core/ to /var/www/

- now you could reboot and see your own MyLazyClock starting !


Questions & Answers
-----------

**Why two installation methods ?**

We choose to purpose two method to install yourself your Lazy Clock because we think you have to have the choice of what browser you want to use. We've also use these two browser and think you could use both of them if you want. But for some choice made by developpers, Chromium on Raspberry Pi isn't the same as Chromium on desktop and so you could find some difference between usage of Firefox and Chromium. For this reason we recommand the usage of Firefox that provide better performance on the Raspberry Pi with our software.

**How to reset completely data of your alarm clock ?**

If you need to reset data of your alarm clock (not remove MyLazyClock, just reset like if you never launch it) :

*Firefox version:*
- copy ~/.mozilla/firefox/XXXXXXXX.default/prefs.js (where "XXXXXXXX.default" is your profil name)
- remove ~/.mozilla/firefox/XXXXXXXX.default/
- start Firefox to generate a new profile (by example, restart your Raspberry Pi)
- replace in the new profile ~/.mozilla/firefox/XXXXXXXX.default/prefs.js by those you previously copy

*Chromium version:*
- remove chromium profile by removing ~/.config/chromium and ~/.cache/chromium


**How to fix blue screen after upgrade/installing the client ?**

If your alarm clock start and only display a blue screen with nothing more, it's because when upgrading you've change access right to to the client. To fix this situation, connect with ssh or connect the SD card directly to a unix/linux system and run following commands :
```
	# for an SD card connected to a unix/linux system
	cd /path/to/sd/card/
	cd var/www
	# for ssh connection to the Raspberry Pi with root user
	cd /var/www
	# all cases
	chown root:root -R *
	chmod go+rx -R *
```

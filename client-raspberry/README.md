myLazyClock - Install Raspberry Pi client
===========

Needed before installation
-----------

- a screen with resolution of 320x240 pixels or more


Easy installation for Raspberry Pi
-----------

You can download our ready-to-use SD card image with MyLazyClock client already install. All necessary drivers for [Adafruit 2.8" TFT screen](https://www.adafruit.com/products/1601) or compatible are installed too. You only need to copy the SD card image on your (micro) SD card, insert it inside your Raspberry Pi, connect screen and ethernet cable then power on it.

SD card could be download here : LINK

Manual installation
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
chromium --disable-session-crashed-bubble --user-agent=raspmylazyclock --kiosk http://localhost
```
- download client from the github and extract it, then copy all content from myLazyClock/client-raspberry/core/ to /srv/http/

- now you could reboot and see your own MyLazyClock starting !


Reset completely data of your alarm clock
-----------

If you need to reset data of your alarm clock (not remove MyLazyClock, just reset like if you never launch it) you could remove chromium profile by removing ~/.config/chromium and ~/.cache/chromium.
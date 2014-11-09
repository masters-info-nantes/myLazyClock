package org.myLazyClock.calendarApi;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
* Send request to get schedule and returns the first
* event of the given day
* 
* Parameters
*  - day in which search first event
* 	- ics file's url
*  - login
*  - password
*  
* @author Jeremy
* 
* @warning Caution with offsets and calendar and see getEdt warning
* 
* External library iCal4j (http://build.mnode.org/projects/ical4j/apidocs/): 
*   - put in eclipse projet (add jar) for local use
*   - in war/WEB-INF/lib to deploy
*   
*/
public class CalendarEdtUnivNantesStrategy implements CalendarStrategy {

    public static final int ID = 2;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Calendar edt univ-nantes";
    }

    /**
     * Get ICS file on the given server
     * @param edtUrl Path to get ics file in schedule
     * @return ICS file as string
     * @throws IOException
     * 
     * @warning Not working with science schedule because of login but
     * 		  working well with staps
     */
    private String getEdt(URL edtUrl) throws IOException {

  		BufferedReader reader = new BufferedReader(new InputStreamReader(edtUrl.openStream()));
  		
  		String line;
  		StringBuilder page = new StringBuilder("");
  		
  		while ((line = reader.readLine()) != null) {
  		    page.append(line);
  		    page.append("\r\n");
  		}
  		reader.close();
  				   
  		return page.toString();
     
  	}
    
    /**
     * Construct calendar from given ICS file
     * and return first event of given day
     * @param url the ur of ics file
     * @param day day in which search event
     * @return First event of the day or null if no events
     * @throws EventNotFoundException if no event found
     */
    @Override
    public Date getFirstEvent (String url, java.util.Calendar day) throws EventNotFoundException {

        String icsFile = null;
        try {
            icsFile = getEdt(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Construct calendar from ICS file
        InputStream is = new ByteArrayInputStream(icsFile.getBytes());
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = null;
        try {
            calendar = builder.build(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }

        // Looking for first event of the day
        ComponentList events = calendar.getComponents(Component.VEVENT);
        VEvent nextEvent = null;
  	  
        for(Object event: events){
            VEvent currEvent = (VEvent)event;
  		  
            java.util.Calendar calCurr = java.util.Calendar.getInstance();
            calCurr.setTime(currEvent.getStartDate().getDate());
  		  
            boolean sameDay = calCurr.get(java.util.Calendar.YEAR) == day.get(java.util.Calendar.YEAR) &&
                    calCurr.get(java.util.Calendar.DAY_OF_YEAR) == day.get(java.util.Calendar.DAY_OF_YEAR);

            if(sameDay){
                // Test if no event found
                boolean currentIsBefore = false;
                if(nextEvent == null){
                    currentIsBefore = calCurr.before(day);
                }
                else {
                    java.util.Calendar calNext = java.util.Calendar.getInstance();
                    calNext.setTime(nextEvent.getStartDate().getDate());
                    currentIsBefore = calCurr.before(calNext);
                }

                if(currentIsBefore){
                    nextEvent = currEvent;
                }
            }
        }

        if (nextEvent == null) {
            throw new EventNotFoundException();
        }

        return nextEvent.getStartDate().getDate();
    }
}

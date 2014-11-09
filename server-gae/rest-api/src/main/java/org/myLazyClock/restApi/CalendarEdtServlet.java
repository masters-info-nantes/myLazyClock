package org.myLazyClock.restApi;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.myLazyClock.calendarApi.EventNotFoundException;
import org.myLazyClock.services.CalendarModulesService;

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
 * @warning Caution with offsets and calendar, see getEdt warning
 * 
 * External library iCal4j (http://build.mnode.org/projects/ical4j/apidocs/): 
 *   - put in eclipse projet (add jar) for local use
 *   - in war/WEB-INF/lib to deploy
 *   
 */
public class CalendarEdtServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	CalendarModulesService serviceCalendar = new CalendarModulesService();
		response.setContentType("text/plain");
		
		// Get given date
		String dateParam = request.getParameter("day");
		if(dateParam == null){
			response.getWriter().println("Please, specify 'day' parameter");
			return;
		}
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		DateFormat dayEvent = new SimpleDateFormat("dd/MM/yyyy");
		
		try{
			cal.setTime(dayEvent.parse(dateParam));	// No offset (month+1) so no offset in getFirstEvent to compare
		}
		catch(ParseException ex){
			ex.printStackTrace();
			response.getWriter().println("Given date isn't well formed: " + dateParam);
			return;
		}
		
		// To search any event before this one
		cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
		cal.set(java.util.Calendar.MINUTE, 59);		
		
		// Get first event of the day
		try {
			Date nextEvent = serviceCalendar.getFirstEventOfDay(2, cal);
	        DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyy HH:mm");
	
	        response.getWriter().println("Event date (offset problems): "
									  + dateTimeFormat.format(nextEvent) + "\n"
			);
		}
		catch(EventNotFoundException ex){
			// No event this day
			response.getWriter().println("No courses today for this day!");
            return;
		}
    }
}

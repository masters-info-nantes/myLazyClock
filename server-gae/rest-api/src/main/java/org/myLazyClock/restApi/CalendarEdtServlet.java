package org.myLazyClock.restApi;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.myLazyClock.services.CalendarModulesService;
import org.myLazyClock.calendarApi.CalendarEdtUnivNantesStrategy;

import net.fortuna.ical4j.model.component.VEvent;

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
        
		// Get given date
		String dateParam = request.getParameter("day");
		if(dateParam == null){
			response.getWriter().println("Please, specify 'day' parameter");
			return;
		}
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try{
			cal.setTime(sdf.parse(dateParam));	// No offset (month+1) so no offset in getFirstEvent to compare
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
		VEvent nextEvent = serviceCalendar.getFirstEventOfDay(CalendarEdtUnivNantesStrategy.ID, cal);
		response.setContentType("text/plain");
		
		if(nextEvent == null){ // No event this day
			response.getWriter().println("No courses today for this day!");
			return;
		}
	
		// Display event start date in human readable form
		Date date = nextEvent.getStartDate().getDate();
		java.util.Calendar debut = java.util.Calendar.getInstance();
		debut.setTime(date);
		
		// Offsets
		//debut.set(java.util.Calendar.MONTH, debut.get(java.util.Calendar.MONTH) + 1);
		//debut.set(java.util.Calendar.HOUR_OF_DAY, debut.get(java.util.Calendar.HOUR_OF_DAY) + 1);
		
		response.getWriter().println("Event date (offset problems): "
								  + debut.get(java.util.Calendar.DAY_OF_MONTH) + "/" +
								  + debut.get(java.util.Calendar.MONTH) + "/" +
								  + debut.get(java.util.Calendar.YEAR) + " " +
								  + debut.get(java.util.Calendar.HOUR_OF_DAY) + ":" +
								  + debut.get(java.util.Calendar.MINUTE) + "\n"								  
		);
		
		// Display event inf
		response.getWriter().println(nextEvent.getDescription().getValue());	
        
    }
}

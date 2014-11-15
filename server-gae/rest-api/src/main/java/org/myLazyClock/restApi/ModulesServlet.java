package org.myLazyClock.restApi;

import org.myLazyClock.services.CalendarModulesService;
import org.myLazyClock.services.TravelModulesServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet to print in plain text all module who implement
 * {@link org.myLazyClock.calendarApi.CalendarStrategy}
 *   and
 * {@link org.myLazyClock.travelApi.TravelStrategy}
 *
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class ModulesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/plain");

        CalendarModulesService serviceCalendar = CalendarModulesService.getInstance();
        TravelModulesServices serviceTravel = new TravelModulesServices();


        PrintWriter out = response.getWriter();

        out.println("All implementation of CalendarStrategy : ");
        out.println("Name of implementation : id");
        out.println(serviceCalendar.listModuleStr());

        out.println();
        out.println("=====================================");
        out.println();

        out.println("All implementation of TravelStrategy : ");
        out.println("Name of implementation : id");
        out.println(serviceTravel.listModule());
    }
}

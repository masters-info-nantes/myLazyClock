package org.myLazyClock.calendarApi;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created on 28/10/14.
 *
 * @author dralagen
 */
public class CalendarFactory implements Iterable<CalendarStrategy> {
    private static CalendarFactory ourInstance = new CalendarFactory();

    public static CalendarFactory getInstance() {
        return ourInstance;
    }

    /**
     * List all module who implement {@link CalendarStrategy}
     */
    private Map<Integer, CalendarStrategy> allModules;

    /**
     * Search all module who implement {@link CalendarStrategy}
     * and put in allModule
     *
     */
    private CalendarFactory() {
        allModules = new HashMap<Integer, CalendarStrategy>();

        Reflections reflections = new Reflections("org.myLazyClock.calendarApi");
        Set<Class<? extends CalendarStrategy>> modules = reflections.getSubTypesOf(CalendarStrategy.class);

        for (Class<? extends CalendarStrategy> aModule : modules) {
            try {
                CalendarStrategy instance = aModule.newInstance();

                Integer id = instance.getId();

                if (allModules.containsKey(id)) {
                    throw new IllegalStateException(
                        "Strategy [" +
                        instance.getName() + "] with id " + id +
                        ", can not be used since the id is already used " +
                        "by strategy [" + allModules.get(id).getName() + "] ");
                }
                allModules.put(id, instance);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Select the {@link CalendarStrategy} with id
     *
     * @param id Identifier of the {@link CalendarStrategy}
     * @return the {@link CalendarStrategy} if find,
     * @throws NullPointerException
     */
    public CalendarStrategy get(Integer id) throws NullPointerException {
        CalendarStrategy strategy = allModules.get(id);
        if (strategy == null) {
            throw new NullPointerException();
        }
        return strategy;
    }

    @Override
    public Iterator<CalendarStrategy> iterator() {
        return allModules.values().iterator();
    }
}

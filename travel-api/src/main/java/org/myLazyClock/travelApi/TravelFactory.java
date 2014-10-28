package org.myLazyClock.travelApi;

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
public class TravelFactory  implements Iterable<TravelStrategy> {
    private static TravelFactory ourInstance = new TravelFactory();

    public static TravelFactory getInstance() {
        return ourInstance;
    }

    private Map<Integer, TravelStrategy> allModules;

    private TravelFactory() {
        allModules = new HashMap<Integer, TravelStrategy>();

        Reflections reflections = new Reflections("org.myLazyClock.travelApi");
        Set<Class<? extends TravelStrategy>> modules = reflections.getSubTypesOf(TravelStrategy.class);

        for (Class<? extends TravelStrategy> aModule : modules) {

            try {
                TravelStrategy instance = aModule.newInstance();

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

    @Override
    public Iterator<TravelStrategy> iterator() {
        return allModules.values().iterator();
    }

    public TravelStrategy get(Integer id) {
        TravelStrategy strategy = allModules.get(id);
        if (strategy == null) {
            throw new NullPointerException();
        }
        return strategy;
    }
}

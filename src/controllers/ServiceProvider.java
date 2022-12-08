package controllers;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Provides a way of supplying singleton instances to service no matter where they are. Simply register the singleton
 * instance in the start of the application, and then get reference to the service whenever needed.
 * Inspiration from https://gist.github.com/FleshMobProductions/5588306c3d554154c65b6b9599eef8af
 */
public class ServiceProvider {

    public static Dictionary<String, Object> InstanceAccessors = new Hashtable<>();

    /**
     * Clears all services registered at time of calling.
     */
    public static void clear() {
        InstanceAccessors = new Hashtable<>();
    }

    /**
     * Creates an instance of the class singleton provided, and registers it to be used in getSingleton.
     * @param singleton The class reference to the type being registered
     */
    public static void addSingleton(Class<?> singleton) {
        if (singleton == null) return;

        String name = singleton.getName();

        try {
            Object instance = singleton.getDeclaredConstructor().newInstance();

            InstanceAccessors.put(name, instance);
        } catch (Exception e) {
            System.out.println("Failed to create singleton instance of " + name);
        }
    }

    /**
     * Retrieves the singleton registered.
     * @param c The class type to retrieve
     * @param <T> The generic type to return
     * @return A singleton instance for the given class type. Returns null if none of that type is registered.
     */
    public static <T> T getSingleton(Class<T> c) {
        String name = c.getName();
        Object instance = InstanceAccessors.get(name);

        return (T)instance;
    }
}

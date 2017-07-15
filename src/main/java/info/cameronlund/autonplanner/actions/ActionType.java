package info.cameronlund.autonplanner.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Name: Cameron Lund
 * Date: 1/19/2017
 * JDK: 1.8.0_101
 * Project: None
 */
public class ActionType {
    private static ArrayList<String> keySet = new ArrayList<>();
    private static HashMap<String, Class<? extends AutonAction>> actions = new HashMap<>();

    public static void registerAction(String name, Class<? extends AutonAction> action) {
        actions.put(name, action);
        keySet.add(name);
    }

    public static AutonAction getInstance(String name, AutonActionWrapper wrapper) {
        Class<? extends AutonAction> c = actions.get(name);
        if (c == null)
            return null;
        try {
            return c.getConstructor(AutonActionWrapper.class).newInstance(wrapper);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Improperly made auton action " + name + ":");
            e.printStackTrace();
        }
        return null;
    }

    public static int indexOf(String name) {
        return keySet.indexOf(name);
    }

    public static Class<? extends AutonAction> getClass(String name) {
        return actions.get(name);
    }

    public static ArrayList<String> getTypesList() {
        return keySet;
    }
}

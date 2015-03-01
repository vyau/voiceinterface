package edu.cmu.sphinx.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import edu.cmu.sphinx.demo.dialog.DialogDemo;


import static java.util.Arrays.copyOfRange;

public class DemoRunner {

    static final Class<?>[] paramTypes = new Class<?>[] {String[].class};
    private static final Map<String, Class<?>> classes =
        new TreeMap<String, Class<?>>();

    static {
        
        classes.put("dialog", DialogDemo.class);
      
    }

    public static void printUsage() {
        System.err.println("Usage: DemoRunner <DEMO> [<ARG> ...]\n");
        System.err.println("Demo names:");

        for (String name : classes.keySet())
            System.err.println("    " + name);
    }

    public static void main(String[] args) throws Throwable {
        if (0 == args.length || !classes.containsKey(args[0])) {
            printUsage();
            System.exit(1);
        }

        try {
            Method main = classes.get(args[0]).getMethod("main", paramTypes);
            main.invoke(null, new Object[]{copyOfRange(args, 1, args.length)});
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}

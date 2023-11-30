package fr._42.reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class App {

    private static Class<?>[] getTypes(Field[] fields) {
        Class<?>[] types = new Class<?>[fields.length];
        for (int i = 0; i < fields.length; i++) {
            types[i] = fields[i].getType();
        }
        return types;
    }

    private static Class<?>[] getTypes(Method[] methods, String methodName) {
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method.getParameterTypes();
            }
        }
        return null;
    }

    private static Object convert(String input, Class<?> type) {
        input = input.trim();
        if (type.equals(String.class)) {
            return input;
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return Integer.parseInt(input);
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return Double.parseDouble(input);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Boolean.parseBoolean(input);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return Long.parseLong(input);
        } else {
            return null;
        }
    }

    private static boolean isOverriddenFromObject(Class<?> clazz, Method method) {
        // if method is declared clazz , we own it
        if (!method.getDeclaringClass().equals(clazz)) {
            return true;
        }

        // Get methods declared in Object class to compare them
        Method[] objectMethods;
        try {
            objectMethods = Object.class.getDeclaredMethods();
        } catch (Exception e) {
            return false;
        }

        // if method is one of the methods from Object
        for (Method objMethod : objectMethods) {
            if (method.getName().equals(objMethod.getName()) &&
                    Arrays.equals(method.getParameterTypes(), objMethod.getParameterTypes())) {
                // Method has same name and parameters as a method in Object, hence it's
                // overridden
                return true;
            }
        }
        // No match found in Object's methods, hence not overidden
        return false;
    }

    public static void repl(String className, List<String> classes, Scanner scanner) throws Exception {
        Class<?> clazz = Class.forName(classes.get(classes.indexOf("fr._42.reflections.classes." + className)));
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println("fields: ");
        for (Field field : fields) {
            System.out.println("    " + field.getType().getSimpleName() + " " + field.getName());
        }
        System.out.println("methods: ");
        for (Method method : methods) {
            if (isOverriddenFromObject(clazz, method)) {
                continue;
            }

            System.out.print("    " + method.getReturnType().getSimpleName() + " " + method.getName() + "(");
            Class<?>[] parameters = method.getParameterTypes();
            for (int i = 0; i < parameters.length; i++) {
                System.out.print(parameters[i].getSimpleName());
                if (i < parameters.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(")");
        }
        System.out.println("---------------------");
        System.err.println("Let's create an object.");
        Constructor<?> constructor = clazz.getConstructor(getTypes(fields));
        Object[] values = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getName() + ": ");
            System.out.print("-> ");
            String input = scanner.nextLine();
            values[i] = convert(input, fields[i].getType());
        }
        Object object = constructor.newInstance(values);
        System.out.println("Object created: " + object);
        System.out.println("---------------------");
        System.err.println("Enter name of the field for changing: ");
        System.out.print("-> ");
        String fieldName = scanner.nextLine();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        System.err.println("Enter " + field.getType().getSimpleName() + " value:");
        System.out.print("-> ");
        String input = scanner.nextLine();
        Object value = convert(input, field.getType());
        field.set(object, value);
        System.err.println("Object updated: " + object);
        System.out.println("---------------------");
        System.err.println("Enter name of the method for call: ");
        System.out.print("-> ");
        String methodName = scanner.nextLine();
        // removing () from method name
        methodName = methodName.substring(0, methodName.indexOf("("));
        Method method = clazz.getDeclaredMethod(methodName, getTypes(methods, methodName));
        method.setAccessible(true);
        Object[] parObject = new Object[method.getParameterCount()];
        for (int i = 0; i < parObject.length; i++) {
            System.out.println("Enter " + method.getParameterTypes()[i].getSimpleName() + " value:");
            System.out.print("-> ");
            input = scanner.nextLine();
            parObject[i] = convert(input, method.getParameterTypes()[i]);
        }
        Object result = method.invoke(object, parObject);
        if (method.getReturnType().equals(Void.TYPE) == false) {
            System.out.println("Method returned:");
            System.out.println(result);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Classes: ");
        List<String> classes = new ArrayList<>();
        classes.add("fr._42.reflections.classes.Car");
        classes.add("fr._42.reflections.classes.Jet");
        classes.add("fr._42.reflections.classes.Motorcycle");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println(classes.get(i).substring(classes.get(i).lastIndexOf(".") + 1));
        }
        System.out.println("---------------------");
        System.out.println("Enter a class name:");
        System.err.print("-> ");
        String className = scanner.nextLine();
        try {
            repl(className, classes, scanner);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            main(args);
        }
        scanner.close();
    }

}

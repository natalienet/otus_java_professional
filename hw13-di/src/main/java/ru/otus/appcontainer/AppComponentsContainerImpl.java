package ru.otus.appcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    //private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    private final Map<Class<?>, Object> appComponentsByClass = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        try {
            processConfig (initialConfigClass);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void processConfig(Class<?> configClass) throws Exception {
        checkConfigClass(configClass);

        List<Method> methodsByOrder = getMethodsByOrder(configClass);

        for (Method method : methodsByOrder) {
            Object appComponent = createAppComponent(configClass, method);
            AppComponent annotation = method.getAnnotation(AppComponent.class);

            if (appComponentsByClass.containsKey(appComponent.getClass())) {
                throw new RuntimeException("The context already contains class " + appComponent.getClass());
            }

            if (appComponentsByName.containsKey(annotation.name())) {
                throw new RuntimeException("The context already contains component name " + annotation.name());
            }

            appComponentsByName.put(annotation.name(), appComponent);
            appComponentsByClass.put(appComponent.getClass(), appComponent);
            for (Class<?> interfaceClass : appComponent.getClass().getInterfaces()) {
                appComponentsByClass.put(interfaceClass, appComponent);
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        if (!appComponentsByClass.containsKey(componentClass)) {
            throw new RuntimeException("Component class " + componentClass.getName() + " is absent in the context");
        }

        return (C) appComponentsByClass.get(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (!appComponentsByName.containsKey(componentName)) {
            throw new RuntimeException("Component name " + componentName + " is absent in the context");
        }
        return (C) appComponentsByName.get(componentName);
    }

    private List<Method> getMethodsByOrder(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private Object createAppComponent(Class<?> configClass, Method method) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = appComponentsByClass.get(parameterTypes[i]);
        }

        Constructor<?> constructor = configClass.getConstructor();
        Object configClassObject = constructor.newInstance();

        return method.invoke(configClassObject, args);
    }
}

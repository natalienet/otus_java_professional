package ru.nn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

class Ioc {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    static TestLoggingInterface createTestClass() {
        InvocationHandler handler = new LoggingHandler(new TestLoggingImpl());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    static class LoggingHandler implements InvocationHandler {
        private final TestLoggingInterface testClass;

        LoggingHandler(TestLoggingInterface testClass) {
            this.testClass = testClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method classMethod = testClass.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (classMethod.isAnnotationPresent(Log.class)) {
                logger.info(
                        "executed method: {}, param: {}",
                        method.getName(),
                        Arrays.stream(args).map(Object::toString).collect(Collectors.joining(",")));
            }
            return method.invoke(testClass, args);
        }
    }
}

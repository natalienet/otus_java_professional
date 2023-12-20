package ru.nn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLoggingImpl implements TestLoggingInterface {
    private static final Logger logger = LoggerFactory.getLogger(TestLoggingImpl.class);

    @Override
    @Log
    public void calculation(int param1) {
        logger.info("calculation, param: {}", param1);
    }

    @Override
    public void calculation(int param1, int param2) {
        logger.info("calculation, param: {}, {}", param1, param2);
    }

    @Override
    public void calculation(int param1, int param2, String param3) {
        logger.info("calculation, param: {}, {}, {}", param1, param2, param3);
    }
}

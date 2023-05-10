
package com.ultrafibra.utilidades;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;


// Clases para terminar proceso de spring en el servidor
@Component
@ComponentScan
public class GracefulShutdownHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private volatile boolean shuttingDown = false;
    private volatile CountDownLatch shutdownLatch = new CountDownLatch(1);

    public void shutdown() {
        logger.info("Received shutdown signal");
        if (!shuttingDown) {
            shuttingDown = true;
            new Thread(() -> {
                try {
                    shutdownLatch.await();
                    logger.info("All threads have finished, shutting down");
                    System.exit(0);
                } catch (InterruptedException e) {
                    logger.error("Error waiting for shutdown latch", e);
                }
            }).start();
            shutdownContext();
        }
    }

    private void shutdownContext() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        if (context instanceof ConfigurableApplicationContext) {
            logger.info("Closing context");
            ((ConfigurableApplicationContext) context).close();
            shutdownLatch.countDown();
        } else {
            logger.warn("Context is not configurable");
            shutdownLatch.countDown();
        }
    }
}

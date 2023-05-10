package com.ultrafibra.utilidades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@SpringBootApplication
public class UltraFibraApplication {

    public static void main(String[] args) {
//		SpringApplication.run(UltraFibraApplication.class, args);
        SpringApplication app = new SpringApplication(UltraFibraApplication.class);
        app.addListeners(new ApplicationPidFileWriter()); //opcional: escribe el PID en un archivo
        ConfigurableApplicationContext context = app.run(args);
        GracefulShutdownHandler gracefulShutdownHandler = context.getBean(GracefulShutdownHandler.class);
        Signal.handle(new Signal("TERM"), new SignalHandler() {
            @Override
            public void handle(Signal signal) {
                gracefulShutdownHandler.shutdown();
            }
        });
    }

}

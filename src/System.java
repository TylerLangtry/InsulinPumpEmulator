import jdk.jshell.execution.Util;

import java.util.Date;
import java.util.concurrent.*;

public class System {

    public static void main(String[] args) {
        Blood blood = new Blood();
        Controller controller = new Controller();
        Pump pump = new Pump();
        Sensor sensor = new Sensor();
        Utilities utilities = new Utilities();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

        // Measure blood sugar every x seconds (10min) and send to controller
        executorService.scheduleAtFixedRate((Runnable) () -> {

            // Task processes go here
            java.lang.System.out.println("[" + new Date() + "] " + "Measure Blood Sugar");
            sensor.measureBloodData(blood);
            if (controller.receiveBloodData(sensor.sendBloodData()).equals("LOW)")) {
                blood.eat();
            }

        }, Config.MEASUREMENT_INTERVAL, Config.MEASUREMENT_INTERVAL, TimeUnit.SECONDS);  // execute every x seconds

        // Calculate required insulin every x seconds (60min) and send to pump for injection
        executorService.scheduleAtFixedRate((Runnable) () -> {

            // Task processes go here
            java.lang.System.out.println("[" + new Date() + "] " + "Inject Insulin");
            pump.receiveCommand(controller.sendInsulinInjection());
            pump.injectInsulin(blood);
            java.lang.System.out.println();

        }, Config.INJECTION_INTERVAL, Config.INJECTION_INTERVAL, TimeUnit.SECONDS);  // execute every x seconds

        // Send data to database every x seconds (30min)
        executorService.scheduleAtFixedRate((Runnable) () -> {

            // Task processes go here
            java.lang.System.out.println("[" + new Date() + "] " + "Send to DB");
            java.lang.System.out.println();

        }, 3, 3, TimeUnit.SECONDS);  // execute every x seconds

        //Code for shutting down executor service
//**********************************************************************************************************************
//        try {
//            java.lang.System.out.println("attempt to shutdown executor");
//            executorService.shutdown();
//            executorService.awaitTermination(5, TimeUnit.SECONDS);
//        }
//        catch (InterruptedException e) {
//            java.lang.System.err.println("tasks interrupted");
//        }
//        finally {
//            if (!executorService.isTerminated()) {
//                java.lang.System.err.println("cancel non-finished tasks");
//            }
//            executorService.shutdownNow();
//            java.lang.System.out.println("shutdown finished");
//        }
//**********************************************************************************************************************

    }
}

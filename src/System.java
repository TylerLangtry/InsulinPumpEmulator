import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class System {

    public static void main(String[] args) {
        Blood blood = new Blood();
        Controller controller = new Controller();
        Pump pump = new Pump();
        Sensor sensor = new Sensor();
        Utilities util = new Utilities();
        ReentrantLock lock = new ReentrantLock();

        util.customUpdate("INSERT INTO Data (data_ID,blood_sug_lvl,collection_date,collection_time,inject_amnt) VALUES ("
               + 0 + "," + Config.STARTING_SUGAR + ",now(),now()," + 0.00 + ")");
        util.closeConnection();
        util.customUpdate("INSERT INTO Status (status_id,battery_charge,reserves_amnt,status_time,status_date) VALUES ("
                + 0 + "," + Config.STARTING_BATTERY + "," + 100 + ",now(),now())");
        util.closeConnection();
        java.lang.System.out.println();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

        // Measure blood sugar every x seconds (10min) and send to controller
        executorService.scheduleAtFixedRate((Runnable) () -> {

            // Task processes go here
            lock.lock();
            try {
                java.lang.System.out.println("[" + new Date() + "] " + "Measure Blood Sugar");
                sensor.measureBloodData(blood);
                double controllerResult = controller.receiveBloodData(sensor.sendBloodData());
                if (controllerResult > 0) {
                    controller.sendStatusData(0, pump.getInsulinAvailable(), controllerResult);
                    blood.eat();
                }
            } finally {
                lock.unlock();
            }
            java.lang.System.out.println();


        }, Config.MEASUREMENT_INTERVAL, Config.MEASUREMENT_INTERVAL, TimeUnit.SECONDS);  // execute every x seconds

        // Calculate required insulin every x seconds (60min) and send to pump for injection
        executorService.scheduleAtFixedRate((Runnable) () -> {

            // Task processes go here
            lock.lock();
            try {
                java.lang.System.out.println("[" + new Date() + "] " + "Inject Insulin");
                double[] controllerResult = controller.sendInsulinInjection();
                double[] pumpResult = pump.receiveCommand(controllerResult[0]);
                if (pumpResult[0] == 0) {
                    pump.injectInsulin(blood);
                } else {
                    pump.fillReserve();
                }
                controller.sendStatusData(pumpResult[0], pumpResult[1], controllerResult[1]);
                if (controllerResult[1] > 0) {
                    util.shutDownExecutors(executorService);
                }

                java.lang.System.out.println();
            } finally {
                lock.unlock();
            }
            java.lang.System.out.println();


        }, Config.INJECTION_INTERVAL, Config.INJECTION_INTERVAL, TimeUnit.SECONDS);  // execute every x seconds
    }
}

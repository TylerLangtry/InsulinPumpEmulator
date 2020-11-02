import java.util.Arrays;
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

        // Database setup (only run if database is empty)
        util.customUpdate("INSERT INTO data (data_ID,blood_sug_lvl,last_update,inj_amnt,users_user_id) VALUES ("
                + 0 + "," + Config.STARTING_SUGAR + ",now()," + 0 + "," + 1 + ")");
        util.closeConnection();
        util.customUpdate("INSERT INTO status (status_id,last_update,battery_charge,reserve_amnt,users_user_id) VALUES ("
                + 0 + ",now()," + Config.STARTING_BATTERY + "," + 100 + "," + 1 +")");
        util.closeConnection();
        util.customUpdate("INSERT INTO configuration (configuration_id, last_change, cooldown_time, max_inj_amnt, min_inj_amnt, max_cumm_dose,users_user_id) VALUES (" +
                "0,now(),5,5,0.1,20,1)");
        util.closeConnection();
        java.lang.System.out.println();

        // Update the config with DB values
        util.updateConfig();
        java.lang.System.out.println("CONFIG UPDATED");
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
                    util.insertData(controllerResult[2], controllerResult[0]);
                    util.closeConnection();
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

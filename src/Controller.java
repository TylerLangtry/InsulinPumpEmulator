import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<Double> bloodData;
    private double bloodSugarTrend;
    private double cumulativeDosage;
    private int batteryStatus;
    private Utilities util = new Utilities();

    public Controller() {
        this.bloodData = new ArrayList<>();
        this.bloodSugarTrend = Config.STARTING_TREND;
        this.batteryStatus = Config.STARTING_BATTERY;
        this.cumulativeDosage = Config.STARTING_CUMULATIVE_DOSE;
    }

    private void analyseBloodSugar() {
        int lastIndex = bloodData.size() - 1;
        if (lastIndex > 5) {
            double recentReadings = (bloodData.get(lastIndex) + bloodData.get(lastIndex - 1) + bloodData.get(lastIndex - 2)) / 3;
            double pastReadings = (bloodData.get(lastIndex - 3) + bloodData.get(lastIndex - 4) + bloodData.get(lastIndex - 5)) / 3;
            this.bloodSugarTrend = recentReadings - pastReadings;
            if (((recentReadings + pastReadings)/2) > (Config.MAX_BLOOD_SUGAR-10)) {
                this.bloodSugarTrend = 1;
            }
        }
        else {
            this.bloodSugarTrend = 0;
        }
    }

    public double receiveBloodData(double bloodSugar) {
        bloodData.add(bloodSugar);
        util.insertData(bloodSugar, 0.00);
        util.closeConnection();
        analyseBloodSugar();
        java.lang.System.out.println("Controller received data");
        if (bloodSugar <= Config.MIN_BLOOD_SUGAR) {
            java.lang.System.out.println("Blood sugar critically low, Eat something! : " + bloodSugar + " mg/dl");
            return 2;
        }
        return 0;
    }

    private double[] calculateInjection() {
        double dosage = 0;
        double dosageErr = 0;
        if (bloodSugarTrend > 0) {
            double bloodSugar = bloodData.get(bloodData.size()-1);
            if (bloodSugar >= Config.INJECTION_THRESHOLD) {
                dosage = util.round((bloodSugar - Config.TARGET_BLOOD_SUGAR)/Config.CORRECTION_FACTOR, 2);
                if (dosage > Config.MAX_INJECTION) {
                    dosage =  Config.MAX_INJECTION;
                }
                else if (dosage < Config.MIN_INJECTION) {
                    dosage = 0;
                }
                if ((cumulativeDosage + dosage) > Config.MAX_CUMULATIVE_DOSE) {
                    java.lang.System.out.println("Calculated dosage exceeds maximum cumulative dosage");
                    dosageErr = 1;
                    dosage = (Config.MAX_CUMULATIVE_DOSE - cumulativeDosage);
                }
                cumulativeDosage += dosage;
                java.lang.System.out.println("Controller calculated an insulin injection of " + dosage + " U-100");
            }
        }
        util.insertData(bloodData.get(bloodData.size() - 1), dosage);
        util.closeConnection();
        return new double[]{dosage,dosageErr};
    }

    public double[] sendInsulinInjection() {
        java.lang.System.out.println("Controller sent insulin injection instructions to Pump");
        return calculateInjection();
    }

    public void sendStatusData(double error, double insulinReserve, double dosageErr) {
        batteryStatus -= 10;
        String errorMsg = "";
        boolean needToRecharge = false;
        if (error > 0) {
            errorMsg += "Insulin Reserves too Low to administer injection;";
        }
        if (dosageErr > 0 && dosageErr < 2) {
            errorMsg += "Dosage exceeds maximum daily dosage;";
        }
        if (dosageErr > 1) {
            errorMsg += "Blood sugar critically low;";
        }
        if (batteryStatus == 10) {
            errorMsg += "Battery Low;";
            needToRecharge = true;
        }
        util.insertStatus(batteryStatus, (int)((insulinReserve/Config.STARTING_INSULIN_STORAGE)*100), errorMsg);
        util.closeConnection();
        if (needToRecharge) {
            // Recharge Battery
            batteryStatus = 100;
        }
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }
}

import java.security.PublicKey;
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

    public String receiveBloodData(double bloodSugar) {
        bloodData.add(bloodSugar);
        java.lang.System.out.println(bloodData);
        analyseBloodSugar();
        java.lang.System.out.println("Controller received data");
        if (bloodSugar <= Config.MIN_BLOOD_SUGAR) {
            java.lang.System.out.println("Blood sugar critically low, Eat something! : " + bloodSugar + " mg/dl");
            return "LOW";
        }
        return "ACK";
    }

    private double calculateInjection() {
        if (bloodSugarTrend > 0) {
            double bloodSugar = bloodData.get(bloodData.size()-1);
            if (bloodSugar >= Config.INJECTION_THRESHOLD) {
                double dosage = Utilities.round((bloodSugar - Config.TARGET_BLOOD_SUGAR)/Config.CORRECTION_FACTOR, 2);
                if (dosage > Config.MAX_INJECTION) {
                    dosage =  Config.MAX_INJECTION;
                }
                else if (dosage < Config.MIN_INJECTION) {
                    dosage = 0;
                }
                if ((cumulativeDosage + dosage) > Config.MAX_CUMULATIVE_DOSE) {
                    java.lang.System.out.println("Calculated dosage exceeds maximum cumulative dosage");
                    dosage = (Config.MAX_CUMULATIVE_DOSE - cumulativeDosage);
                }
                cumulativeDosage += dosage;
                java.lang.System.out.println("Controller calculated an insulin injection of " + dosage + " U-100");
                return dosage;
            }
        }
            return 0;
    }

    public double sendInsulinInjection() {
        java.lang.System.out.println("Controller sent insulin injection instructions to Pump");
        return calculateInjection();
    }

    private String sendAlert() {
        return "ALERT";
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public void pushToDB() {

    }
}

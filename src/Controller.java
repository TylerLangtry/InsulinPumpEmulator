import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<Double> bloodData;
    private double bloodSugarTrend;
    private double maxInjection;
    private double minInjection;
    private double maxCummulativeDose;
    private int batteryStatus;
    private Utilities util = new Utilities();

    public Controller() {
        this.bloodData = new ArrayList<>();
        this.bloodSugarTrend = 0;
        this.batteryStatus = 100;
        setRules();
    }

    public void setRules() {
        this.maxInjection = 10;
        this.minInjection = 1;
        this.maxCummulativeDose = 100;
    }

    private void analyseBloodSugar() {
        int lastIndex = bloodData.size() - 1;
        if (lastIndex > 5) {
            double recentReadings = (bloodData.get(lastIndex) + bloodData.get(lastIndex - 1) + bloodData.get(lastIndex - 2)) / 3;
            double pastReadings = (bloodData.get(lastIndex - 3) + bloodData.get(lastIndex - 4) + bloodData.get(lastIndex - 5)) / 3;
            this.bloodSugarTrend = recentReadings - pastReadings;
            if (((recentReadings + pastReadings)/2) > (250-10)) {
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
        if (bloodSugar <= 70) {
            java.lang.System.out.println("Blood sugar critically low, Eat something! : " + bloodSugar + " mg/dl");
            return "LOW";
        }
        return "ACK";
    }

    private double calculateInjection() {
        if (bloodSugarTrend > 0) {
            double bloodSugar = bloodData.get(bloodData.size()-1);
            if (bloodSugar >= 150) {
                double dosage = util.round((bloodSugar - 100)/50, 2);
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

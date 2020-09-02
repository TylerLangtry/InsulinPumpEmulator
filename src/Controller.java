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
        this.bloodSugarTrend = 2;
    }

    public String receiveBloodData(double bloodSugar) {
        bloodData.add(bloodSugar);
        analyseBloodSugar();
        java.lang.System.out.println("Controller received data");
        return "ACK";
    }

    private double calculateInjection() {
        java.lang.System.out.println("Controller calculated an insulin injection of " + bloodData.get(0) /2 + "ml");
        java.lang.System.out.println("Controller sent insulin injection instructions to Pump");
        return bloodData.get(0) /2;
    }

    public double sendInsulinInjection() {
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

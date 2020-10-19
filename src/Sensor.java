public class Sensor {
    private double bloodData;


    public Sensor() {
        this.bloodData = 0;
    }

    public void measureBloodData(Blood blood) {
        this.bloodData = blood.getBloodSugar();
        java.lang.System.out.println("Sensor measured blood sugar level of " + bloodData + " mg/dl");

    }

    public double sendBloodData() {
        java.lang.System.out.println("Sensor sent data to Controller");
        return bloodData;
    }
}

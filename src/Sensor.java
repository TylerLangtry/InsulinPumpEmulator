public class Sensor {
    private double bloodData;

    public Sensor() {
        this.bloodData = 0;
    }

    public void measureBloodData(Blood blood) {
        this.bloodData = blood.getBloodSugar();
        System.out.println("test");
    }

    public double sendBloodData() {
        return bloodData;
    }
}

public class Blood {
    private double bloodSugar;

    Blood() {
        this.bloodSugar = 10;
    }

    public double getBloodSugar() {
        return bloodSugar;
    }

    public void amnmendBloodSugar(double insulin) {
        this.bloodSugar = this.bloodSugar - insulin;
    }
}

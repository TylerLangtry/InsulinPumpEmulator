import java.util.Random;

public class Blood {
    private double bloodSugar;
    private double insulin;
    private double reductionFactor;
    private int count;
    private Random random = new Random();
    private Utilities util = new Utilities();

    Blood() {
        this.bloodSugar = 100;
        this.insulin = 0;

    }

    public double getBloodSugar() {
        count++;
        if (count == 30) {
            this.bloodSugar = bloodSugar + 150;
            this.count = 0;
        }
        if (insulin < 0.1) {
            this.insulin = 0;
        }
        if (insulin > 0) {
            this.insulin = insulin - reductionFactor;
            this.bloodSugar = bloodSugar - (reductionFactor * 50);
        }
        return util.round(bloodSugar + (random.nextInt(10)-5), 2);
    }

    public void amendBloodSugar(double insulin) {
        this.insulin = this.insulin + insulin;
        this.reductionFactor = util.round(insulin/20, 2);
    }

    public void eat() {
        this.bloodSugar = bloodSugar + 150;
        this.count = 0;
    }
}

import java.util.Random;

public class Blood {
    private double bloodSugar;
    private double insulin;
    private double reductionFactor;
    private int count;
    private Random random = new Random();
    private Utilities util = new Utilities();

    Blood() {
        this.bloodSugar = Config.STARTING_SUGAR;
        this.insulin = Config.STARTING_INSULIN;

    }

    public double getBloodSugar() {
        java.lang.System.out.println("BLOOD INSULIN LEVEL: " + insulin);
        count++;
        if (count == Config.EATING_CYCLE) {
            this.bloodSugar = bloodSugar + Config.FOOD_SUGAR;
            this.count = 0;
        }
        if (insulin < 0.1) {
            this.insulin = 0;
        }
        if (insulin > 0) {
            this.insulin = insulin - reductionFactor;
            this.bloodSugar = bloodSugar - (reductionFactor * Config.CORRECTION_FACTOR);
        }
        return util.round(bloodSugar + (random.nextInt(10)-5), 2);
    }

    public void amendBloodSugar(double insulinInjection) {
        java.lang.System.out.println("OLD INSULIN: " + this.insulin);
        this.insulin += insulinInjection;
        java.lang.System.out.println("NEW INSULIN: " + this.insulin);
        this.reductionFactor = util.round(this.insulin/20, 2);
    }

    public void eat() {
        this.bloodSugar = bloodSugar + Config.FOOD_SUGAR;
        this.count = 0;
    }
}

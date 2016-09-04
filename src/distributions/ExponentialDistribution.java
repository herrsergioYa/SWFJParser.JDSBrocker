package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 27.08.2016.
 */
public class ExponentialDistribution implements Distribution {
    private double mean;

    public ExponentialDistribution(double mean) {
        this.mean = mean;
    }

    @Override
    public double next(Random random) {
        return generate(random, mean);
    }

    public static double generate(Random random, double mean) {
        return - mean * Math.log(1.0 - random.nextDouble());
    }

}

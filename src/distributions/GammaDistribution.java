package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 20.08.2016.
 */
public class GammaDistribution implements Distribution {

    private double mean, order;

    public GammaDistribution(double mean, double order) {
        this.mean = mean;
        this.order = order;
    }

    @Override
    public double next(Random random) {
        return generate(random, mean, order);
    }

    public static double generate(Random random, double mean, double order1) {
        if(order1 > 50) {
            return mean * Math.abs(1.0 + random.nextGaussian() / Math.sqrt(order1));
        }

        if(order1 <= 0) {
            throw new IllegalStateException();
        }

        double order = order1;
        double value = 0.0;

        while(order >= 1.0) {
            value -= Math.log(1.0 - random.nextDouble());
            order -= 1.0;
        }

        if (order > 0.0) {
            value -= getBeta(random, order) * Math.log(1.0 - random.nextDouble());;
        }

        return mean * value / order1;
    }

    public static double getBeta(Random random, double a, double b) {
        double x;
        double y;
        double z;
        
        do {
            x = Math.pow(random.nextDouble(), 1/a);
            y = Math.pow(random.nextDouble(), 1/b);
            z = x + y;
        } while(z == 0 || z > 1);
        
        return x/z;
    }

    public static double getBeta(Random random, double m) {
        return getBeta(random, m, 1.0 - m);
    }
}

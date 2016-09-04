package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 27.08.2016.
 */
public class HypoGammaDistribution implements Distribution {
    private double means[];
    private double[] orders;

    public HypoGammaDistribution(double[] means, double[] orders) {
        this.means = means;
        this.orders = orders;
        if(means.length != orders.length)
            throw new IllegalArgumentException();
    }

    @Override
    public double next(Random random) {
        return generate(random, means, orders);
    }

    public static double generate(Random random, double[] means, double[] orders) {
        double value = 0;
        for(int i = 0; i < means.length; i++) {
            value += GammaDistribution.generate(random, means[i], orders[i]);
        }
        return value;
    }
}

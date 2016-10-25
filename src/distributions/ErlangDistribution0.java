package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 27.08.2016.
 */
public class ErlangDistribution0 implements Distribution {
    private double mean;
    private int order;

    public ErlangDistribution0(double mean, int order) {
        this.mean = mean;
        this.order = order;
    }

    @Override
    public double next(Random random) {
        return generate(random, mean, order);
    }

    public static double generate(Random random, double mean, int order) {
        return ErlangDistribution.generate(random, mean, order + 1);
    }

    @Override
    public double getDF(double t, boolean cdf) {
        return GammaDistribution.getDF((order + 1) / mean, order + 1, t, cdf);
    }
}

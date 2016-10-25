package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 18.08.2016.
 */
public class Markovian implements Distribution {

    private double e;

    public Markovian(double e) {
        this.e = e;
    }

    @Override
    public double next(Random random) {
        return - e * Math.log(1.0 - random.nextDouble());
    }

    @Override
    public double getDF(double t, boolean cdf) {
        return ExponentialDistribution.getDF(1.0 / e, t, cdf);
    }
    
    
}

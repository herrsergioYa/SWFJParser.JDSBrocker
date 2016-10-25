package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 19.08.2016.
 */
public class ConstantDistribution implements Distribution {

    private double value;

    public ConstantDistribution(double value) {
        this.value = value;
    }

    @Override
    public double next(Random random) {
        return value;
    }

    @Override
    public double getDF(double t, boolean cdf) {
        if(cdf) {
            return t > value ? 1 : 0;
        } else {
            return t == value ? Double.POSITIVE_INFINITY : 0.0;
        }
    }

    
}

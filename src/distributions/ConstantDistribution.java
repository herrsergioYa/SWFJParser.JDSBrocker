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

}

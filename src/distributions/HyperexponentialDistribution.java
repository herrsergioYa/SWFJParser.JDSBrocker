package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 27.08.2016.
 */
public class HyperexponentialDistribution implements Distribution {

    private double alphas[], means[];

    public HyperexponentialDistribution(double[] alphas, double[] means) {
        this.alphas = alphas;
        this.means = means;
    }

    @Override
    public double next(Random random) {
        return generate(random, alphas, means);
    }

    public static double generate(Random random, double[] alphas, double[] means) {
        int event = PhaseDistribution.sim(random, alphas);
        if(event >= means.length)
            return 0;
        else
            return ExponentialDistribution.generate(random, means[event]);
    }
    
    @Override
    public double getDF(double t, boolean cdf) {
        double sum = 0.0;
        for(int i = 0; i < alphas.length; i++) {
            sum += alphas[i] * ExponentialDistribution.getDF(1.0/ means[i], t, cdf);
        }
        return sum;
    }
}

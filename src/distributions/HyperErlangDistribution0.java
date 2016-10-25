package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 27.08.2016.
 */
public class HyperErlangDistribution0 implements Distribution {
    private double[] alphas;
    private double[] means;
    private int[] orders;

    public HyperErlangDistribution0(double[] alphas, double[] means, int[] orders) {
        this.alphas = alphas;
        this.means = means;
        this.orders = orders;
        if(means.length != orders.length)
            throw new IllegalArgumentException();
    }

    public static double generate(Random random, double[] alphas, double[] means, int[] orders) {
        int event = PhaseDistribution.sim(random, alphas);
        if(event >= means.length)
            return 0;
        else
            return ErlangDistribution0.generate(random, means[event], orders[event]);
    }

    @Override
    public double next(Random random) {
        return generate(random, alphas, means, orders);
    }
    
    @Override
    public double getDF(double t, boolean cdf) {
        double sum = 0.0;
        for(int i = 0; i < alphas.length; i++) {
            sum += alphas[i] * GammaDistribution.getDF((orders[i] + 1) / means[i], orders[i] + 1, t, cdf);
        }
        return sum;
    }
}

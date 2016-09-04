package distributions;

import java.util.Random;

/**
 * Created by HerrSergio on 20.08.2016.
 */
public class PhaseDistribution implements Distribution {

    private double[] alphas;
    private double[][] lambdas;
    private double[][] ps;

    public PhaseDistribution(double[] alphas, double[][] lambdas) {
        this.alphas = alphas;
        this.lambdas = lambdas;
        this.ps = new double[lambdas.length][];
        for(int i = 0; i < lambdas.length; i++) {
            double l = - lambdas[i][i];
            if(l < 0)
                throw new IllegalArgumentException();
            ps[i] = new double[lambdas.length];
            if(l == 0.0)
                continue;
            for(int j = 0; j < lambdas.length; j++) {
                if(j != i) {
                    ps[i][j] = lambdas[i][j] / l;
                }
            }
        }
    }

    public static int sim(Random random, double[] ps)  {
        double p = random.nextDouble();
        for(int i = 0 ; i < ps.length; i++) {
            p -= ps[i];
            if(p < 0.0)
                return i;
        }
        return ps.length;
    }

    @Override
    public double next(Random random) {

        double value = 0.0;

        for(int i = sim(random, alphas);
            i >= 0 && i < lambdas.length;
            i = sim(random, ps[i])) {

            double l = -lambdas[i][i];

            if(l <= 0)
                return Double.POSITIVE_INFINITY;

            value -= Math.log(1.0 - random.nextDouble()) / l;
        }

        return value;
    }
}

package distributions;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Created by HerrSergio on 28.08.2016.
 */
public class SemiPhaseDistribution implements Distribution {

    private double[] alphas;
    private Distribution[][] distr;

    public SemiPhaseDistribution(double[] alphas, Distribution[][] distr) {
        this.alphas = alphas;
        this.distr = distr;
    }

    @Override
    public double next(Random random) {

        double value = 0.0;

        for(int i = PhaseDistribution.sim(random, alphas);
            i >= 0 && i < distr.length;
                ) {

            double newValue = Double.POSITIVE_INFINITY;
            int event= - 1;
            for(int j = 0; j < distr[i].length; j++) {
                double buf = distr[i][j].next(random);
                if(buf < newValue) {
                    newValue = buf;
                    event = j;
                }
            }

            value += newValue;
            i = event;
        }

        return value;
    }
}

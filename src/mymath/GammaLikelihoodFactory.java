/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymath;

import distributions.Distribution;
import distributions.GammaDistribution;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author HerrSergio
 */
public class GammaLikelihoodFactory implements DistributionFactory {

    @Override
    public Distribution get(double[] datas) {
        double mean = Statistic.getMean(datas);
        //Arrays.stream(datas).mapToObj((e) -> (Double) e).collect(Collectors.toList()));
        double lmean = Arrays.stream(datas).mapToObj(e -> Math.log(Math.max(e, Approx.MIN_VALUE))).collect(Collectors.averagingDouble(d -> d));
        double data = lmean - Math.log(mean);

        double x = 1.0, step = 0.1, value = Math.abs(get(x, data));
        while (step > 0.00000000001) {
            double left = x - step, right = x + step;
            double lvalue = Math.abs(get(left, data));
            double rvalue = Math.abs(get(right, data));
            
            while (lvalue < value) {
                value = lvalue;
                x = left;

                left -= step;
                lvalue = Math.abs(get(left, data));

            }
            
            while (rvalue < value) {
                value = rvalue;
                x = right;

                right += step;
                rvalue = Math.abs(get(right, data));

            }

            step /= 2;

        }

        return new GammaDistribution(mean, x);
    }

    private static double get(double nu, double data) {
        return Gamma.digamma(nu) - Math.log(nu) - data;
    }
}

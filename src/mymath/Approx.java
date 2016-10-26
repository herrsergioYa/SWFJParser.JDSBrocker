/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymath;

import distributions.Distribution;
import distributions.HyperGammaDistribution;
import hooke_jeeves.Function;
import hooke_jeeves.HookeJeeves;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * @author HerrSergio
 */
public class Approx {

    private Approx() {

    }

    public static double[] hyperGamma(double[] data, int branchesCount) {
        HyperGammaLikelihoodFunction func = new HyperGammaLikelihoodFunction(branchesCount).initData(data);
        double[] start = new double[func.argsCount()];
        double[] step = new double[func.argsCount()];
        double mean = Statistic.getMean(data);
//Arrays.stream(data).mapToObj((e) -> (Double) e).collect(Collectors.toList()));
        for (int i = 0; i < branchesCount; i++) {
            start[i] = 1.0 / mean;
            step[i] = start[i] / 100.0;
            start[i] = 0.01 / mean;
            step[i] = start[i] / 10.0;
        }
        for (int i = branchesCount; i < branchesCount * 2; i++) {
            start[i] = 1.0;
            step[i] = 0.1;
        }
        for (int i = branchesCount * 2; i < branchesCount * 3 - 1; i++) {
            start[i] = 1.0 / branchesCount;
            step[i] = start[i] / 100.0;
        }
        double[] d = HookeJeeves.minimize(func, start, step, 2048);
        return d;
    }

    public static double gammaPdf(double t, double lambda, double order) {
        //lambda *= order; 
        return Math.pow(lambda, order) * Math.pow(t, order - 1) * Math.exp(-t * lambda) / Gamma.gamma(order);
    }

    private static double hyperGammaPdf(double t, double[] values) {
        if (t < 0) {
            return 0.0;
        }
        int count = (values.length + 1) / 3;
        double sum = 0.0, sub = 1.0;
        for (int i = 0; i < count; i++) {
            double prop;
            if (i == count - 1) {
                prop = sub;
            } else {
                prop = values[count * 2 + i];
                sub -= prop;
            }
            if (sub < 0.0 || prop < 0.0 || prop > 1.0 || values[i] <= 0.0 || values[count + i] <= 0) {
                return 0.0;
            }
            sum += gammaPdf(t, values[i], values[count + i]) * prop;
        }
        return sum;
    }

    public static final double MIN_VALUE = 0.00001;
    
    public static class HyperGammaLikelihoodFunction implements LikelihoodFunction {

        private double[] data;
        private int branchesCount, count;

        public HyperGammaLikelihoodFunction(int branchesCount, int count) {
            this.branchesCount = branchesCount;
            this.count = count;
        }
         
        public HyperGammaLikelihoodFunction(int branchesCount) {
            this(branchesCount, 24);
        }
            

        protected HyperGammaLikelihoodFunction(HyperGammaLikelihoodFunction function, double[] data) {
            this(function.branchesCount, function.count);
            this.data = data; 
        }
        
        @Override
        public double[] getData() {
            return data;
        }

        @Override
        public HyperGammaLikelihoodFunction initData(double[] data) {
            return new HyperGammaLikelihoodFunction(this, data);
        }
        
        @Override
        public double get(double[] args) {
            double sum = 0.0;
            for (int i = 0; i < data.length; i++) {
                double d = Math.max(MIN_VALUE, data[i]);
                sum += Math.log(hyperGammaPdf(d, args));
            }
            return -sum;
        }

        @Override
        public int argsCount() {
            return branchesCount * 3 - 1;
        }

        @Override
        public Distribution getDistribution(double[] ars) {
            int count = (ars.length + 1) / 3;
            double alphas[] = new double[count];
            double means[] = new double[count];
            double orders[] = new double[count];
            double sub = 1.0;
            for(int i = 0; i < count; i++) {
                if(i != count - 1) {
                    sub -= ars[count * 2 + i];
                    alphas[i] = ars[count * 2 + i];
                } else {
                    alphas[i] = sub;
                }
                orders[i] = ars[count + i];
                means[i] = orders[i] / ars[i];
            }
            return new HyperGammaDistribution(alphas, means, orders);
        }

        @Override
        public double[][] getStartStep() {
            double[] start = new double[argsCount()];
            double[] step = new double[argsCount()];
            double mean = Statistic.getMean(data);
                    //Arrays.stream(data).mapToObj((e) -> (Double) e).collect(Collectors.toList()));
            for (int i = 0; i < branchesCount; i++) {
                start[i] = 1.0 / mean;
                step[i] = start[i] / 100.0;
                start[i] = 0.01 / mean;
                step[i] = start[i] / 10.0;
            }
            for (int i = branchesCount; i < branchesCount * 2; i++) {
                start[i] = 1.0;
                step[i] = 0.1;
            }
            for (int i = branchesCount * 2; i < branchesCount * 3 - 1; i++) {
                start[i] = 1.0 / branchesCount;
                step[i] = start[i] / 100.0;
            }
            return new double[][] {start, step, new double[] {count}};
        }
    }
}

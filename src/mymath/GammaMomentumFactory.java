/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymath;

import distributions.Distribution;
import distributions.GammaDistribution;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author HerrSergio
 */
public class GammaMomentumFactory implements DistributionFactory {

    @Override
    public Distribution get(double[] datas) {
        //List<Double> data = Arrays.stream(datas).mapToObj((e) -> (Double) e).collect(Collectors.toList());
        double mean = Statistic.getMean(datas);
        double variance = Statistic.getUnbiasedVariance(datas);
        double order = mean * mean / variance;
        return new GammaDistribution(mean, order);
            
    }
    
}

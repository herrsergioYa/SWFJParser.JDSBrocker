/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymath;

import distributions.Distribution;
import distributions.ExponentialDistribution;

/**
 *
 * @author HerrSergio
 */
public class MarkovianFactory implements DistributionFactory {

    @Override
    public Distribution get(double[] datas) {
        return new ExponentialDistribution(Statistic.getMean(datas));
    }
    
}

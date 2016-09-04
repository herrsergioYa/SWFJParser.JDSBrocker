/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymath;

import distributions.Distribution;
import hooke_jeeves.HookeJeeves;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author HerrSergio
 */
public class LikelihoodFactory implements DistributionFactory {

    private LikelihoodFunction function;

    public LikelihoodFactory() {
    }

    public LikelihoodFactory(LikelihoodFunction function) {
        this.function = function;
    }

    public LikelihoodFunction getFunction() {
        return function;
    }

    public void setFunction(LikelihoodFunction function) {
        this.function = function;
    }

    @Override
    public Distribution get(double[] datas) {
        double[] oldData = function.getData();
        try {
            function.setData(datas);
            double[][] startStep = function.getStartStep();
            double[] d = HookeJeeves.minimize(function, startStep[0], startStep[1], (int)Math.round(startStep[2][0]));
            return function.getDistribution(d);
        } finally {
            function.setData(oldData);
        }
    }

}

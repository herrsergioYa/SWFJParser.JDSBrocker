/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymath;

import distributions.Distribution;
import hooke_jeeves.Function;


/**
 *
 * @author HerrSergio
 */
public interface LikelihoodFunction extends Function {
    Distribution getDistribution(double[] ars);
    double[][] getStartStep();
    double[] getData();
    LikelihoodFunction initData(double[] data);
}

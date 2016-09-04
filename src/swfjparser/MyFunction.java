/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import hooke_jeeves.Function;

/**
 *
 * @author HerrSergio
 */
public class MyFunction implements Function {

    @Override
    public double get(double[] args) {
        return Math.sin(args[0]) * Math.cos(args[1]);
    }

    @Override
    public int argsCount() {
        return 2;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input.factories;

import java.util.Arrays;
import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swftasks.input.HazardTaskInput;
import swftasks.input.SimpleTaskInput;
import swftasks.TaskInput;

/**
 *
 * @author HerrSergio
 */
public class HazardTaskInputFactory extends AbstractTaskInputFactory {

    private boolean norm;
    
    public HazardTaskInputFactory(boolean norm, DistributionFactory factory, int... width) {
        super(factory, width);
        this.norm = norm;
    }

    public HazardTaskInputFactory(DistributionFactory factory, int... width) {
        this(true, factory, width);
    }
    
    @Override
    protected TaskInput extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory) {
        double[][] d = file.getWeights(norm, minWidth, maxWidth);
        //Arrays.fill(d[0], 1.0);
        if (d[1].length != 0) {
            return new HazardTaskInput(
                    new SimpleTaskInput(file.getWidths(minWidth, maxWidth), factory.get(d[1])),
                    d[0],
                    d[3][0]
            );
        } else {
            return null;
        }
    }
    
}

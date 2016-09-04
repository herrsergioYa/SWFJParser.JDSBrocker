/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swftasks.input.SimpleTaskInput;
import swftasks.TaskInput;

/**
 *
 * @author HerrSergio
 */
public class SimpleTaskInputFactory extends AbstractTaskInputFactory {

    public SimpleTaskInputFactory(DistributionFactory factory, int... width) {
        super(factory, width);
    }    
   
    @Override
    protected TaskInput extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory) {
        double[] d = file.getIntervals(minWidth, maxWidth);
        if (d.length != 0) {
            return new SimpleTaskInput(file.getWidths(minWidth, maxWidth), factory.get(d));
        } else {
            return null;
        }
    }
    
    
}

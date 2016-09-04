/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.complicity.factories;

import distributions.Distribution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swftasks.complicity.ComplicityDistribution;
import swftasks.complicity.DefaultRigidTaskComplicity;
import swftasks.TaskInput;
import swftasks.RigidTaskComplicity;

/**
 *
 * @author HerrSergio
 */
public class SquareTaskComplicityFactory extends AbstractRigidTaskComplicityFactory {

    public SquareTaskComplicityFactory(DistributionFactory factory, int... width) {
        super(factory, width);
    }

    protected ComplicityDistribution extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory) {
        double[] d = file.getSquares(minWidth, maxWidth);
        if(d.length != 0)
            return new ComplicityDistribution(true, factory.get(d));
        else
            return null;
    }
    
}

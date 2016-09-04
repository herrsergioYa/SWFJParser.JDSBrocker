/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.complicity.factories;

import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swftasks.complicity.ComplicityDistribution;

/**
 *
 * @author HerrSergio
 */
public class LengthTaskComplicityFactory extends AbstractRigidTaskComplicityFactory {

    public LengthTaskComplicityFactory(DistributionFactory factory, int... width) {
        super(factory, width);
    }

    protected ComplicityDistribution extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory) {
        double[] d = file.getLengths(minWidth, maxWidth);
        if(d.length != 0)
            return new ComplicityDistribution(false, factory.get(d));
        else
            return null;
    }
        
}

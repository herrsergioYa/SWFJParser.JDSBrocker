/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.complicity.factories;

import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swftasks.complicity.ComplicityDistribution;
import swftasks.complicity.DeterministicTaskComplicityDistribution;

/**
 *
 * @author HerrSergio
 */
public class DeterministicTaskComplicityFactory extends AbstractRigidTaskComplicityFactory {

    private boolean square;
    
    public DeterministicTaskComplicityFactory(boolean square, int... width) {
        super(null, width);
        this.square = square;
    }

    @Override
    protected ComplicityDistribution extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory) {
        double[] d = square ? file.getSquares(minWidth, maxWidth) : file.getLengths(minWidth, maxWidth);
        return new DeterministicTaskComplicityDistribution(square, d);
    }
    
}

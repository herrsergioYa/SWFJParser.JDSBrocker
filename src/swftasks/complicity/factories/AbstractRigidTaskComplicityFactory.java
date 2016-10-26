/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.complicity.factories;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swftasks.RigidTaskComplicityFactory;
import swftasks.complicity.ComplicityDistribution;
import swftasks.complicity.DefaultRigidTaskComplicity;

/**
 *
 * @author HerrSergio
 */
public abstract class AbstractRigidTaskComplicityFactory implements RigidTaskComplicityFactory {
    
    private DistributionFactory factory;
    private TreeSet<Integer> widths;
    
    public AbstractRigidTaskComplicityFactory(DistributionFactory factory, int... width) { 
        this.factory = factory;
        this.widths = new TreeSet<>(Arrays.stream(width).mapToObj(i -> (Integer)i).collect(Collectors.toList()));
        this.widths.add(Integer.MAX_VALUE);
    }

    @Override
    public DefaultRigidTaskComplicity get(GWFFile file) {
        TreeMap<Integer, ComplicityDistribution> data = new TreeMap<>();
        int last = 0;
        begin(file);
        for (int j : widths) {
            ComplicityDistribution distribution = extract(file, last + 1, j, factory);
            if (distribution != null) {
                data.put(j, distribution);
            }
            last = j;
        }
        end(file);
        return new DefaultRigidTaskComplicity(data);
    }

    protected void begin(GWFFile file) {
    }

    protected void end(GWFFile file) {
    }

    protected abstract ComplicityDistribution extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory);

    TreeSet<Integer> getWidths() {
        return widths;
    }

    DistributionFactory getFactory() {
        return factory;
    }
    
    
    
}

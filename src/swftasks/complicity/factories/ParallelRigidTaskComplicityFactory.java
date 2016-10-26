/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.complicity.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import swfjparser.GWFFile;
import swftasks.ParallelFactory;
import swftasks.RigidTaskComplicity;
import swftasks.RigidTaskComplicityFactory;
import swftasks.complicity.ComplicityDistribution;
import swftasks.complicity.DefaultRigidTaskComplicity;

/**
 *
 * @author HerrSergio
 */
public class ParallelRigidTaskComplicityFactory implements RigidTaskComplicityFactory, ParallelFactory {

    private ExecutorService service;
    private Map<Integer, Future<ComplicityDistribution>> toAwait;
    private AbstractRigidTaskComplicityFactory complicity;

    public ParallelRigidTaskComplicityFactory(ExecutorService service, AbstractRigidTaskComplicityFactory complicity) {
        this.service = service;
        this.complicity = complicity;
    }
    
    @Override
    public RigidTaskComplicity get(GWFFile file) {
        if (toAwait == null) {
            beginGet(file);
        }
        TreeMap<Integer, ComplicityDistribution> data = new TreeMap<>();
        for (int j : toAwait.keySet()) {
            try {
                ComplicityDistribution distribution = toAwait.get(j).get();
                if (distribution != null) {
                    data.put(j, distribution);
                }
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(ParallelRigidTaskComplicityFactory.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        toAwait = null;
        complicity.end(file);
        return new DefaultRigidTaskComplicity(data);
    }

    @Override
    public void beginGet(GWFFile file) {
        if (toAwait == null) {
            toAwait = new TreeMap<>();
            complicity.begin(file);
            int last = 0;
            for (int j : complicity.getWidths()) {
                final int newLast = last + 1;
                toAwait.put(j, service.submit(() -> complicity.extract(file, newLast, j, complicity.getFactory())));
                last = j;
            }
        }

    }

}

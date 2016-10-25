/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.complicity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import swftasks.RigidTaskComplicity;

/**
 *
 * @author HerrSergio
 */
public class DefaultRigidTaskComplicity implements RigidTaskComplicity {
    
    private TreeMap<Integer, ComplicityDistribution> streams;

    public DefaultRigidTaskComplicity(TreeMap<Integer, ComplicityDistribution> streams) {
        this.streams = streams;
    }

    @Override
    public double get(Random random, int width, boolean square) {
        int key = streams.ceilingKey(width);
        return streams.get(key).get(random, width, square);
    }
    
    public Map<Integer, ComplicityDistribution> getDistributions() {
        LinkedHashMap<Integer, ComplicityDistribution> map = new LinkedHashMap<>();
        int last = 1;
        for(Map.Entry<Integer, ComplicityDistribution> entry : streams.entrySet()) {
            //String key = last + "_" + entry.getKey();
            //last = entry.getKey() + 1;
            //map.put(key, entry.getValue());
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}

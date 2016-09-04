/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swfjparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import swftasks.RigidTask;

/**
 *
 * @author HerrSergio
 */
public class CustomSWFFile extends GWFFile implements Collection<RigidTask> {
    
    private ArrayList<RigidTask> tasks = new ArrayList<>();
    private Comparator<RigidTask> comp = new Comparator<RigidTask>() {
        @Override
        public int compare(RigidTask t, RigidTask t1) {
            return Double.compare(t.getIncomeTime(), t1.getIncomeTime());
        }
    };

    public CustomSWFFile() {
    }
    

    @Override
    public double getTime(int row) {
        return tasks.get(row).getIncomeTime();
    }

    @Override
    public int getWidth(int row) {
        return tasks.get(row).getWidth();
    }

    @Override
    public double getLength(int row) {
        return tasks.get(row).getLength();
    }

    @Override
    public double getSquare(int row) {
        return getLength(row) * getSquare(row);
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int size() {
        return tasks.size();
    }

    @Override
    public boolean isEmpty() {
        return tasks.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return tasks.contains(o);
    }

    @Override
    public Iterator<RigidTask> iterator() {
        return new Iterator<RigidTask>() {
            
            private ListIterator<RigidTask> iter = tasks.listIterator();
            
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public RigidTask next() {
                return iter.next();
            }
        };
    }

    @Override
    public Object[] toArray() {
        return tasks.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return tasks.toArray(ts);
    }

    @Override
    public boolean add(RigidTask e) {
        int index = Collections.binarySearch(tasks, e, comp);
        if(index >= 0) {
            while(index < tasks.size() && comp.compare(tasks.get(index), e) == 0)
                index++;
        } else {
            index = -1 - index;
        }
        tasks.add(index, e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return tasks.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        return tasks.containsAll(clctn);
    }

    @Override
    public boolean addAll(Collection<? extends RigidTask> clctn) {
        for(RigidTask info : clctn) {
            add(info);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        return tasks.removeAll(clctn);
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        boolean changed = false;
        for(int i = 0; i < tasks.size(); ) {
            RigidTask task = tasks.get(i);
            if(!clctn.contains(task)) {    
                tasks.remove(i);
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        tasks.clear();
    }

    @Override
    public String toString() {
       /* return toString(5);
    }
    
    public String toString(int fieldWidth) {*/
        StringBuilder sb = new StringBuilder();
        
        String sep = System.lineSeparator();
        
        sb.append("; SWF file by PhD Gaevoy Sergey").append(sep);
        sb.append("; VSTU, Volgograd, Russia").append(sep);
        sb.append("; Created with SWFParser/JDSBrocker").append(sep);
        
        sb.append(";").append(sep);
        
        for(int i = 0; i < tasks.size(); i++) {
            RigidTask task = tasks.get(i);
            sb.append(String.format(
                    "\t%*d\t%*.0f\t%*d\t%*.0f\t%*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d\t%6$*d".replaceAll("\\*", "" /*+ fieldWidth*/),
                    i + 1, task.getIncomeTime(), -1, task.getLength(), task.getWidth(), -1
                    ));
            sb.append(sep);
        }
            
        return sb.toString();
    }
    
    
}

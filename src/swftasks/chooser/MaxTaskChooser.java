/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.chooser;

import swftasks.Task;


/**
 *
 * @author HerrSergio
 */
public class MaxTaskChooser extends AbstarctTaskChooser {

    public MaxTaskChooser(boolean allowCut) {
        super(allowCut);
    }

    @Override
    protected int getWidth(Task task) {
        return task.getMaxWidth();
    }
    
}

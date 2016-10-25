/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.model.impl;

import swftasks.TaskStream;
import swftasks.model.AbstractModel;
import swftasks.model.InputData;
import swftasks.model.OutputData;

/**
 *
 * @author HerrSergio
 */
public class DefaultModel extends AbstractModel {

    public DefaultModel(InputData inputData, TaskStream taskStream) {
        super(inputData, taskStream);
    }  
    
    @Override
    public OutputData getResult() {
        return new OutputData();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

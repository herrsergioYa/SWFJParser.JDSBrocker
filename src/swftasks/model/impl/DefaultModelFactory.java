/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.model.impl;

import swftasks.FastTaskStreamFactory;
import swftasks.model.InputData;
import swftasks.model.Model;
import swftasks.model.ModelFactory;

/**
 *
 * @author HerrSergio
 */
public class DefaultModelFactory implements ModelFactory {

    private InputData inputData;
    private FastTaskStreamFactory taskStreamFactory;

    public DefaultModelFactory(InputData inputData, FastTaskStreamFactory taskStreamFactory) {
        this.inputData = inputData;
        this.taskStreamFactory = taskStreamFactory;
    }
    
    public InputData getInputData() {
        return inputData;
    }

    public void setInputData(InputData inputData) {
        this.inputData = inputData;
    }    

    public FastTaskStreamFactory getTaskStreamFactory() {
        return taskStreamFactory;
    }

    public void setTaskStreamFactory(FastTaskStreamFactory taskStreamFactory) {
        this.taskStreamFactory = taskStreamFactory;
    }
    
    @Override
    public Model get() {
        return new DefaultModel(inputData, taskStreamFactory.get());
    }
    
}

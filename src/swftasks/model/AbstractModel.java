/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.model;

import java.util.concurrent.Callable;
import swftasks.TaskStream;
import swftasks.streams.DefaultFastTaskStreamFactory;

/**
 *
 * @author HerrSergio
 */
public abstract class AbstractModel implements Model {
    private double currentTime;      
    private InputData inputData;
    private TaskStream taskStream;

    public AbstractModel(InputData inputData, TaskStream taskStream) {
        this.inputData = inputData;
        this.taskStream = taskStream;
    }

    
    //@Override
    protected InputData getInputData() {
        return inputData;
    }   

    protected TaskStream getTaskStream() {
        return taskStream;
    }
    
    
    
    @Override
    public OutputData call() {
        run();
        return getResult();
    } 
    
     
    @Override
    public double getCurrentTime() {
        return currentTime;
    }

    protected void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }
    
}

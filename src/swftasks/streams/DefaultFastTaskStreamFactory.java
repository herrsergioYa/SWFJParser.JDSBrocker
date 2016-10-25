/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.streams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import swftasks.FastTaskStreamFactory;
import swftasks.TaskStream;

/**
 *
 * @author HerrSergio
 */
public class DefaultFastTaskStreamFactory<T extends TaskStream> implements FastTaskStreamFactory<T> {

    private byte[] bytes;

    public DefaultFastTaskStreamFactory(TaskStream taskStream) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream output = new ObjectOutputStream(outputStream) ) {
            output.writeObject(taskStream);
            output.flush();
            outputStream.flush();
            bytes = outputStream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    
    @Override
    public T get() {
        try(ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream input = new ObjectInputStream(inputStream) ) {
            return (T)input.readObject();
        } catch (IOException | ClassNotFoundException  ex) {
            throw new RuntimeException(ex);
        } 
    }
    
}

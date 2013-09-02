/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.io.IOException;

/**
 *
 * @author vvv
 */
public class Singletone<T>
{
    private final Object lock = new Object();
    private volatile T singletone = null;
    private SingletoneCreator<T> creator = null;
    
    static public interface SingletoneCreator<T>
    {
        public T create() throws IOException;
    }
    
    public Singletone( SingletoneCreator<T> creator)
    {
        this.creator = creator;
    }
    
    public void set(T value)
    {
        this.singletone = value;
    }
    
    public T get() throws IOException
    {
        if( singletone == null )
        {
            synchronized(lock)
            {
                if( singletone == null)
                {
                    singletone = creator.create();
                }
            }
        }
        return singletone;
    }

}

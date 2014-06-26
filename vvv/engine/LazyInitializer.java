/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

/**
 *
 * @author vvv
 * @param <T>
 */
public class LazyInitializer<T>
{
    private final Object lock = new Object();
    private volatile T singletone = null;
    private Creator<T> creator = null;
    
    static public interface Creator<T>
    {
        public T create() throws Exception;
    }
    
    public LazyInitializer( Creator<T> creator)
    {
        this.creator = creator;
    }
    
    public void set(T value)
    {
        if( value == null )
        {
            throw new IllegalArgumentException("Value must be non null");
        }
        this.singletone = value;
    }
    
    public T get() throws Exception
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

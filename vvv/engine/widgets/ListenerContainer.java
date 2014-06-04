package vvv.engine.widgets;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author QwertyVVV
 */
public class ListenerContainer
{
    private final List<ActionListener> list = new LinkedList<>();
    
    public synchronized void addListener( ActionListener listener )
    {
        if( !list.contains( listener) )
        {
            list.add(listener);
        }
    }
    
    public synchronized void removeListener( ActionListener listener)
    {
        list.remove(listener);
    }
    
    public synchronized void action()
    {
        for( ActionListener al: list )
        {
            al.action();
        }
    }
    
}

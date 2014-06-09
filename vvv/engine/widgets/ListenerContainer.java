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
        final int size = list.size();
        for( int i = 0; i < size; ++i )
        {
            final ActionListener al = list.get(i);
            al.action();
        }
    }
    
}

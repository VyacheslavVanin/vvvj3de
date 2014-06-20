/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import java.util.List;

/**
 *
 * @author Вячеслав
 */
public abstract class Panel extends Widget
{
    public boolean addWidget( Widget wgt )
    {
        if( addChild(wgt) )
        {
            onAddWidget(wgt);
            return true;
        }
        return false;
    }
    
    public boolean removeWidget( Widget wgt )
    {
        boolean b =  deleteChild(wgt);
        if( b )
        {
            onRemoveWidget(wgt);
        }
        return b;
    }



    @Override
    protected boolean onMouseButtonDown(int button, int x, int y)
    {
        if( isContainPoint(x, y) )
        {
            final List<Widget> list = getChildren();
            final int s = list.size();
            for( int i =0; i < s; ++i )
            {
                final Widget wgt = list.get(i);
                if( wgt.invokeMouseButtonDown(button, x, y) )
                {
                    return true;
                } 
            }   
        }
        return false;
    }

    @Override
    protected void onMouseMove(int x, int y)
    {
        final List<Widget> list = getChildren();
        final int s = list.size();
        for( int i =0; i < s; ++i )
        {
            final Widget wgt = list.get(i);
            wgt.invokeMouseMove(x, y);      
        }   
    }

    @Override
    protected void onMouseButtonUp(int button, int x, int y)
    {
        final List<Widget> list = getChildren();
        final int s = list.size();
        for( int i =0; i < s; ++i )
        {
            final Widget wgt = list.get(i);
            wgt.invokeMouseButtonUp( button, x, y);
        }   
    }

  
    @Override
    protected void onSetSize(int w, int h)
    { }   
    
    abstract protected void onAddWidget( Widget wgt );
    abstract protected void onRemoveWidget( Widget wgt );
}

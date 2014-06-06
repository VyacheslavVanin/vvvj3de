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
            wgt.onAttach();
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
    protected void onLeftMouseButtonDown(float x, float y)
    {
        if( isContainPoint(x, y) )
        {
            List<Widget> list = getChildren();
            for( Widget wgt : list )
            {
                wgt.invokeLeftMouseButtonDown(x, y); 
            }   
        }
    }

    @Override
    protected void onMouseMove(float x, float y)
    {
        List<Widget> list = getChildren();
        for( Widget wgt : list )
        {
             wgt.invokeMouseMove(x, y);      
        }   
    }

    @Override
    protected void onLeftMouseButtonUp(float x, float y)
    {
        if( isContainPoint(x, y) )
        {
            List<Widget> list = getChildren();
            for( Widget wgt : list )
            {
                 wgt.invokeLeftMouseButtonUp(x, y);
            }   
        }
    }

  
    @Override
    protected void onSetSize(float w, float h)
    { }   
    
    abstract protected void onAddWidget( Widget wgt );
    abstract protected void onRemoveWidget( Widget wgt );
}

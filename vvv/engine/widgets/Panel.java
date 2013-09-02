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
        if( !addChild(wgt) )
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
    boolean onLeftMouseButtonDown(float x, float y)
    {
        if( isContainPoint(x, y) )
        {
            List<Widget> list = getChildren();
            for( Widget wgt : list )
            {
                wgt.onLeftMouseButtonDown(x, y); 
            }   
        }
        return false;
    }

    @Override
    boolean onMouseMove(float x, float y)
    {
        List<Widget> list = getChildren();
        for( Widget wgt : list )
        {
             wgt.onMouseMove(x, y);      
        }   
    
        return false;
    }

    @Override
    boolean onLeftMouseButtonUp(float x, float y)
    {
         if( isContainPoint(x, y) )
        {
            List<Widget> list = getChildren();
            for( Widget wgt : list )
            {
                 wgt.onLeftMouseButtonUp(x, y);
            }   
        }
        return false;
    }

    
   
    
    
    @Override
    protected void onSetPosition(float x, float y)
    {

    }

    @Override
    protected void onSetSize(float w, float h)
    {
        
    }   
    
    abstract protected void onAddWidget( Widget wgt );
    abstract protected void onRemoveWidget( Widget wgt );
}

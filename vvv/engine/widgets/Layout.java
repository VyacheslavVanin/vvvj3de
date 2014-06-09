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
public abstract class Layout extends Panel
{
    @Override
    protected void onAddWidget(Widget wgt)
    {
        rearrange();
    }

    @Override
    protected void onRemoveWidget(Widget wgt)
    {
        rearrange();
    }

    @Override
    protected void onDraw() throws Exception
    {
        
    }
    
    public final void moveWidgets( Layout other )
    {
        List<Widget> l = getChildren();
        for( Widget w : l )
        {
            other.addChild(w);
        }
        other.rearrange();
    }
    
    protected void rearrange() 
    {
        List<Widget> l = getChildren();
        for( Widget w : l )
        {
            if( w instanceof Layout )
            {
                w.setSize( getWidth(), getHeight() );
            }
        }
    };
    
    @Override
    protected void onSetSize(float w, float h)
    {
        super.onSetSize(w, h); //To change body of generated methods, choose Tools | Templates.
        
        
        rearrange();
    }

    @Override
    protected void onSetPosition(float x, float y)
    {
        super.onSetPosition(x, y); //To change body of generated methods, choose Tools | Templates.
        rearrange();
    }  
    
    @Override
    protected void onAttach()
    {
        final float h =  parent.getHeight();
        final float w = parent.getWidth();
        setSize(w, h);
    }
    
}

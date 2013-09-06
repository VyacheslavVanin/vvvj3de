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
    
    protected abstract void rearrange();
    
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
}

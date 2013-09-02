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
public class TestPanel extends Panel
{
    private float widgetsHeight = 0;
   
    @Override
    protected void onAddWidget(Widget wgt)
    {
        wgt.setPosition(0, widgetsHeight);
        widgetsHeight += wgt.getHeight();
    }

    @Override
    protected void onRemoveWidget(Widget wgt)
    {
        widgetsHeight -= wgt.getHeight();
    }

    @Override
    protected void onDraw() throws Exception
    {
        float h = 0;
        List<Widget> list = getChildren();
        for( Widget wgt : list )
        {
            h += wgt.getHeight() + 2;
            wgt.setPosition(0, h);
        }
    }  
}

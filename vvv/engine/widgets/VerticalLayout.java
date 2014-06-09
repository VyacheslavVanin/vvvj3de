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
public class VerticalLayout extends Layout
{
    @Override
    protected void rearrange()
    {
        super.rearrange();
        List<Widget> l = getChildren();
        float fullWeight = 0;
        
        final int size = l.size();
        for( int i = 0; i < size; ++i)
        {
            final Widget wgt = l.get(i);
            fullWeight += wgt.getHeight();
        }
        
        float fullHeight = getHeight();
        float h = fullHeight;
        for( int i = 0; i < size; ++i)
        {
            final Widget wgt = l.get(i);
            float weight = wgt.getHeight() / fullWeight;
            float partHeight = fullHeight * weight; // height of part
            h -= partHeight;
            float offsetY = ( partHeight - wgt.getHeight()) / 2;
            float offsetX = ( getWidth() - wgt.getWidth() ) / 2;
            
            wgt.setPosition( offsetX, (h + offsetY));
        }

    }   
}

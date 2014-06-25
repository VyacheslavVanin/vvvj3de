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
public class HorizontalLayout extends Layout
{
    @Override
    protected void rearrange()
    {
        super.rearrange();
        List<Widget> l = getChildren();
        float fullWeight = 0;
        
        final int size = l.size();
        for( int i =0; i < size; ++i )
        {
            final Widget wgt = l.get(i);
            fullWeight += wgt.getWidth();
        }
        
        float fullWidth = getWidth();
        int h = 0;
        for( int i =0; i < size; ++i )
        {
            final Widget wgt = l.get(i);
            float weight = wgt.getWidth() / fullWeight;
            int partWidth = (int)(fullWidth * weight); // height of part
            
            
            int offsetY = (int)(( getHeight() - wgt.getHeight()) / 2);
            int offsetX = (int)(( partWidth - wgt.getWidth() ) / 2);
            
            wgt.setPosition( (h + offsetX), offsetY);
            h += partWidth;
        }
    }  

}

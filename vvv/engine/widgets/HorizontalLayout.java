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
        List<Widget> l = getChildren();
        float fullWeight = 0;
        for( Widget wgt: l)
        {
            fullWeight += wgt.getWidth();
        }
        
        float fullWidth = getWidth();
        float h = 0;
        for( Widget wgt: l)
        {
            float weight = wgt.getWidth() / fullWeight;
            float partWidth = fullWidth * weight; // height of part
            
            
            float offsetY = (( getHeight() - wgt.getHeight()) / 2);
            float offsetX = (( partWidth - wgt.getWidth() ) / 2);
            
            wgt.setPosition( (h + offsetX), offsetY);
            h += partWidth;
        }
    }  
}

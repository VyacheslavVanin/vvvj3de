/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vvv.engine.colorRectangle;

import vvv.engine.Color;
import vvv.engine.ConstColor;
import vvv.engine.widgets.RectangularGraphicObject;

/**
 *
 * @author vvv
 */
public class SolidColorRectangle extends RectangularGraphicObject
{
    private Color color = ConstColor.DARK_GRAY ;
    
    public final void setColor( Color c)
    {
        color = c;
    }
    
    public final void setColor( float red, float green, float blue, float alpha)
    {
        color = new ConstColor(red, green, blue, alpha);
    }
    
    public final Color getColor()
    {
        return color;
    }
    
    
    
}

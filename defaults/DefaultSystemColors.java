/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package defaults;

import vvv.engine.ConstColor;
import vvv.engine.VariableColor;

/**
 *
 * @author vvv
 */
public class DefaultSystemColors 
{
    private static final VariableColor panelColor  = new VariableColor( ConstColor.WHITE );
    private static final VariableColor controlColor = new VariableColor( ConstColor.WHITE );
    private static final VariableColor textColor   = new VariableColor( ConstColor.BLACK );
    
    public static VariableColor getPanelColor() { return panelColor; }
    public static VariableColor getControlColor() { return controlColor; }
    public static VariableColor getTextColor() { return textColor; }
    
}

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
    
    private static final VariableColor sliderActivePart  = new VariableColor( ConstColor.BLUE );
    private static final VariableColor sliderPassivePart = new VariableColor( ConstColor.GRAY );
    
    public static VariableColor getPanelColor() { return panelColor; }
    public static VariableColor getControlColor() { return controlColor; }
    public static VariableColor getTextColor() { return textColor; }
    public static VariableColor getSliderActivePartColor() {return sliderActivePart; }
    public static VariableColor getSliderPassivePartColor() {return sliderPassivePart; }
    
}

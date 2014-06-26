/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.util.logging.Level;
import java.util.logging.Logger;
import vvv.engine.texture.Texture;
import vvv.engine.widgets.AbstractButton;

/**
 * ??? Чего я хотел дублируя этот класс из default buttona?
 * @author QwertyVVV
 */
public abstract class RectangleButton extends AbstractButton 
{
    private Texture checked = null;
    private Texture unchecked = null;
    private Texture texture = null;


    public RectangleButton() {
        try {
            checked = DefaultGui.getButtonCheckedTexture();
            unchecked = DefaultGui.getButtonUncheckedTexture();
        } catch (Exception ex) {
            Logger.getLogger(DefaultPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        texture = unchecked;
    }



    @Override
    protected void onPress() {
        texture = checked;
    }

    @Override
    protected void onRelease() {
        texture = unchecked;
    }


}

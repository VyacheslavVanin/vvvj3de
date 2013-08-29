/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.layers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vvv.engine.Defaults;
import vvv.engine.texture.Texture;

/**
 *
 * @author QwertyVVV
 */
public class TestCheckBox extends AbstractCheckBox
{
    private TextLabel   text    = new TextLabel("CheckBox");
    private ImageWidget image = new ImageWidget();
    private Texture     textureChecked = null; 
    private Texture     textureUnchecked = null;
    
    public TestCheckBox()
    {
        addChild(text);
        addChild(image);
        try
        {
            textureChecked   = Defaults.getCheckBoxCheckedTexture();
            textureUnchecked = Defaults.getCheckBoxUncheckedTexture();
        }
        catch(IOException ex)
        {
            Logger.getLogger(TestCheckBox.class.getName()).log(Level.SEVERE, null, ex);
        }

        image.setTexture( textureUnchecked );
        
        text.setColor(1, 1, 1, 1);
        
        setWidth( image.getWidth() + 3 + text.getWidth());
        setHeight( image.getHeight());
        onSetPosition(0, 0);
    }
    
    @Override
    protected void onCheck()
    {
        image.setTexture(textureChecked);
    }

    @Override
    protected void onUncheck()
    {
        image.setTexture(textureUnchecked);
    }

    @Override
    protected void onMouseEnter()
    {
       
    }

    @Override
    protected void onMouseLeave()
    {
       
    }

    @Override
    protected void onPress()
    {
       
    }

    @Override
    protected void onRelease()
    {
    }

    @Override
    protected void onDraw() throws Exception
    {
    }

    @Override
    protected void onSetPosition(float x, float y)
    {
        image.setPosition(0, 0);
        text.setPosition( image.getPosX() + image.getWidth() + 3,
                          0);
    }
    
}

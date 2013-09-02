package vvv.engine.widgets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import defaults.gui;
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
            textureChecked   = gui.getCheckBoxCheckedTexture();
            textureUnchecked = gui.getCheckBoxUncheckedTexture();
        }
        catch(IOException ex)
        {
            Logger.getLogger(TestCheckBox.class.getName()).log(Level.SEVERE, null, ex);
        }

        image.setTexture( textureUnchecked );
        
        text.setColor(0.5f, 0.5f, 0.5f, 1);
        
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
       text.setColor(1, 1, 1, 1);
    }

    @Override
    protected void onMouseLeave()
    {
       text.setColor(0.5f, 0.5f, 0.5f, 1);
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
        float tH = text.getHeight();
        float iH = image.getHeight();
        float dy = (tH - iH ) /2;
        image.setPosition(0, dy);
        text.setPosition( image.getPosX() + image.getWidth() + 3,
                          0);
    }

    @Override
    protected void onSetSize(float w, float h)
    {
        
    }
    
}

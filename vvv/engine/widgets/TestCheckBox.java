package vvv.engine.widgets;

import defaults.DefaultGui;
import java.util.logging.Level;
import java.util.logging.Logger;
import vvv.engine.ConstColor;
import vvv.engine.texture.Texture;

/**
 *
 * @author QwertyVVV
 */
public class TestCheckBox extends AbstractCheckBox
{
    private final TextLabel   text    = new TextLabel("CheckBox");
    private final ImageWidget image = new ImageWidget();
    private Texture     textureChecked = null; 
    private Texture     textureUnchecked = null;
    
    public TestCheckBox()
    {
        addChild(text);
        addChild(image);
        try
        {
            textureChecked   = DefaultGui.getCheckBoxCheckedTexture();
            textureUnchecked = DefaultGui.getCheckBoxUncheckedTexture();
        }
        catch(Exception ex)
        {
            Logger.getLogger(TestCheckBox.class.getName()).log(Level.SEVERE, null, ex);
        }

        image.setTexture( textureUnchecked );
        
        text.setColor( ConstColor.GRAY );
        
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
       text.setColor( ConstColor.WHITE );
    }

    @Override
    protected void onMouseLeave()
    {
       text.setColor( ConstColor.GRAY );
    }


    @Override
    protected void onDraw() throws Exception
    {
    }

    @Override
    protected void onRefresh() {
        int tH = text.getHeight();
        int iH = image.getHeight();
        int dy = (tH - iH ) /2;
        image.setPosition(0, dy);
        text.setPosition( image.getPosX() + image.getWidth() + 3,
                          0);
    }
    
}

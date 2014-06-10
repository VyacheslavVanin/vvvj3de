/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vvv.engine.text.Font;
import vvv.engine.texture.Texture;
import vvv.engine.widgets.AbstractCheckBox;
import vvv.engine.widgets.ImageWidget;
import vvv.engine.widgets.TextLabel;

/**
 *
 * @author QwertyVVV
 */
public class DefaultCheckbox extends AbstractCheckBox
{
    private final TextLabel text;
    private final ImageWidget box;
    private Texture   checked = null;
    private Texture   unchecked = null;
    private boolean   autosize = true;
    
    
    public DefaultCheckbox()
    {
        try
        {
            checked   = DefaultGui.getCheckBoxCheckedTexture();
            unchecked = DefaultGui.getCheckBoxUncheckedTexture();
        }
        catch(IOException ex)
        {
            Logger.getLogger(DefaultCheckbox.class.getName()).log(Level.SEVERE, null, ex);
        }  
           
        box = new ImageWidget(unchecked);
        box.setSize(16, 16);
        text = new TextLabel("Checkbox");
        addChild(text);
        addChild(box);
        setOptimalSize();
    }
    
    public DefaultCheckbox(String str)
    {
        this();
        text.setText( str );
        setOptimalSize();
    }
    
    
    private float calcWidth()
    {
        float ret = box.getWidth() + 
                    SPACE_BETWEN_BOX_AND_TEXT +
                    text.getWidth();
        return ret;
    }
    
    public void setAutoSize(boolean b)
    {
        this.autosize = b;
    }
    
    private void setOptimalSize()
    {
        final float boxHeight = box.getHeight();
        final float textHeight = text.getHeight();
        final float h = Math.max( boxHeight, textHeight);
        final float w = calcWidth();
        setSize(w, h);
    }
    
    public void setText(String str)
    {
        text.setText(str);
        if( autosize )
        {
            setOptimalSize();
        }
    }
    
    public void setFont(Font font)
    {
        text.setFont(font);
        if( autosize )
        {
            setOptimalSize();
        }
    }
    
    @Override
    protected void onCheck()
    {
        box.setTexture(checked);
    }

    @Override
    protected void onUncheck()
    {
        box.setTexture(unchecked);
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

    private final static float SPACE_BETWEN_BOX_AND_TEXT = 3;
    private void placeWidets()
    {
        float boxOffsetY = (getHeight() - box.getHeight())*0.5f;
        box.setPosition( 0, boxOffsetY);
        
        float textOffsetX = box.getWidth() + SPACE_BETWEN_BOX_AND_TEXT;
        float textOffsetY = (getHeight() - text.getHeight())*0.5f;
        text.setPosition(textOffsetX, textOffsetY);
    }
    
    @Override
    protected void onSetPosition(float x, float y)
    {
        placeWidets();
    }

    @Override
    protected void onSetSize(float w, float h)
    {
        placeWidets();
    }
    
}

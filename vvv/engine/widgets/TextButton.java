package vvv.engine.widgets;

import vvv.engine.ConstColor;
import vvv.engine.text.Font;

/**
 *
 * @author Вячеслав
 */
public class TextButton extends AbstractButton
{
    private final TextLabel text = new TextLabel("Button");
 
    public TextButton()
    {
        addChild(text);
        text.setColor( ConstColor.GRAY );
        setSize(text.getWidth(), text.getHeight());
        setPosition(0, 0);
    }
    
    
    public void setText( String text)
    {
        this.text.setText(text);
    }
    
    public void setFont( Font font )
    {
        this.text.setFont(font);
    }
    
    
    
    @Override
    protected void onDraw() throws Exception
    {       
    }

    @Override
    protected void onSetPosition(float x, float y)
    {
        text.setPosition(0, 0);
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
    protected void onPress()
    {
        text.setPosition(0, -2);
    }

    @Override
    protected void onRelease()
    {
        text.setPosition(0, 0);
    }


    @Override
    protected void onRefresh() {
        final int w = getWidth();
        final int h = getHeight();
        text.setSize(w, h);
    }
}

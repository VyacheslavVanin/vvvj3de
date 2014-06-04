package vvv.engine.widgets;

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
        text.setColor(0.5f, 0.5f, 0.5f, 1);
        setSize(text.getWidth(), text.getHeight());
        setPosition(0, 0);
    }
    
    
    
    public void setTextColor(float r, float g, float b, float a)
    {
        text.setColor(r, g, b, a);
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
        setTextColor(1, 1, 1, 1);
    }

    @Override
    protected void onMouseLeave()
    {
        setTextColor(0.5f, 0.5f, 0.5f, 1);
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
    protected void onSetSize(float w, float h)
    {
        text.setSize(w, h);
    }
}

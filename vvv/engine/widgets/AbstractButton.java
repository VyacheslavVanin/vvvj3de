package vvv.engine.widgets;

import org.lwjgl.input.Keyboard;

/**
 *
 * @author vvv
 */
public abstract class AbstractButton extends Widget
{
    private final ListenerContainer onClickListener   = new ListenerContainer();
    private final ListenerContainer onPressListener   = new ListenerContainer();
    private final ListenerContainer onReleaseListener = new ListenerContainer();
    
    
    public final void addOnClickListener( ActionListener listener )
    {
        this.onClickListener.addListener(listener);
    }
    
    public final void removeOnClickListener( ActionListener listener )
    {
        this.onClickListener.removeListener(listener);
    }
    

    public final void addOnPressListener( ActionListener listener )
    {
        this.onPressListener.addListener(listener);
    }
    
    public final void removeOnPressListener( ActionListener listener)
    {
        this.onPressListener.removeListener(listener);
    }
    
    
    public final void addOnReleaseListener( ActionListener listener)
    {
        this.onReleaseListener.addListener(listener);
    }
    
    public final void removeOnReleaseListener( ActionListener listener)
    {
        this.onReleaseListener.removeListener(listener);
    }
    
    protected  void onClick() {}
    protected  void onPress() {}
    protected  void onRelease() {}
    
    private void onClickBase()
    {
        onClick();
        onClickListener.action();
    }
    
    private void onPressBase()
    {
        onPress();
        onPressListener.action();
    }
    
    private void onReleaseBase()
    {
        onRelease();
        onReleaseListener.action(); 
    }
    
    
    
    private boolean leftButtonHold = false;
    
    protected final boolean isLeftButtonHeld() 
    {
        return leftButtonHold; 
    }
    
    @Override 
    protected void onMouseEnter()
    {
        if (leftButtonHold ) 
        {
            onPressBase();
        }
    }
    
    @Override
    protected void onMouseLeave()
    {
        if( leftButtonHold )
        {
            onReleaseBase();
        }
    }
    
    @Override
    protected final boolean onMouseButtonDown( int button, float x, float y) 
    {
        if( button == 0 && isContainPoint(x, y) )
        {
            leftButtonHold = true;
            onPressBase();
            return true;
        }
        return false;
    }
    
    @Override
    protected final void onMouseButtonUp( int button, float x, float y) 
    {
        if( button == 0 )
        {
            if( leftButtonHold && isContainPoint(x, y))
            {
                onReleaseBase();
                onClickBase();
            }
            else
            {
                onReleaseBase();
            }
            leftButtonHold = false;
        }
    }
    
    @Override
    protected final void onKeyPress(int key, char ch)
    {
         switch( key )
         {
             case Keyboard.KEY_SPACE:
             case Keyboard.KEY_RETURN:
                 onClickBase();
                 break;
             default:
                 break;
         }
    }
    
}

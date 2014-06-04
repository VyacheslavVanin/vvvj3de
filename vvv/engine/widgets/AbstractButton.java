package vvv.engine.widgets;

/**
 *
 * @author vvv
 */
public abstract class AbstractButton extends Widget
{
    private final ListenerContainer onClickListener      = new ListenerContainer();
    private final ListenerContainer onEnterListener      = new ListenerContainer();
    private final ListenerContainer onLeaveListener      = new ListenerContainer();
    private final ListenerContainer onPressListener      = new ListenerContainer();
    private final ListenerContainer onReleaseListener    = new ListenerContainer();
    
    
    public final void addOnClickListener( ActionListener listener )
    {
        this.onClickListener.addListener(listener);
    }
    
    public final void removeOnClickListener( ActionListener listener )
    {
        this.onClickListener.removeListener(listener);
    }
    
    
    public final void addOnEnterListener( ActionListener listener)
    {
        this.onEnterListener.addListener(listener);
    }
        
    public final void removeOnEnterListener( ActionListener listener)
    {
        this.onEnterListener.removeListener(listener);
    }
    
    
    public final void addOnLeaveListener( ActionListener listener )
    {
        this.onLeaveListener.addListener(listener);
    }
    
    public final void removeOnLeaveListener( ActionListener listener)
    {
        this.onLeaveListener.removeListener(listener);
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
    
    protected  void onClick() {};
    protected  void onMouseEnter() {};
    protected  void onMouseLeave() {};
    protected  void onPress() {};
    protected  void onRelease() {};
    
    private void onClickBase()
    {
        onClick();
        onClickListener.action();
    }
    
    private void onMouseEnterBase()
    {  
        onMouseEnter();
        onEnterListener.action();
    }
    
    private void onMouseLeaveBase()
    {  
        onMouseLeave();
        onLeaveListener.action();
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
    
    protected final boolean isMouseInArea()    { return inArea;         }
    protected final boolean isLeftButtonHold() { return leftButtonHold; }
    
    private boolean inArea         = false;
    private boolean leftButtonHold = false;
    
    @Override
    protected final void onMouseMove( float x, float y )
    { 
        if( isContainPoint(x, y) )
        {
            if( !inArea )
            {
                inArea = true;
                onMouseEnterBase();
                if( leftButtonHold )
                {
                    onPressBase();
                }
            }
        }
        else
        {
            if( inArea )
            {
                inArea = false;
                onMouseLeaveBase();
                if( leftButtonHold )
                {
                    onReleaseBase();
                }
            }    
        }
    }
    
    @Override
    protected final void onLeftMouseButtonDown( float x, float y) 
    {
        if( isContainPoint(x, y) )
        {
            leftButtonHold = true;
            onPressBase();
        }
    }
    
    @Override
    protected final void onLeftMouseButtonUp( float x, float y) 
    {
        if( isContainPoint(x, y))
        {
            if( leftButtonHold )
            {
                onReleaseBase();
                onClickBase();
            }
            else
            {
                onReleaseBase();
            }
        }
        leftButtonHold = false;
    }
    
}

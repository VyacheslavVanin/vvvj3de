package vvv.engine.widgets;

/**
 *
 * @author vvv
 */
public abstract class AbstractButton extends Widget
{
    private ListenerContainer onClickListener      = new ListenerContainer();
    private ListenerContainer onEnterListener      = new ListenerContainer();
    private ListenerContainer onLeaveListener      = new ListenerContainer();
    private ListenerContainer onPressListener      = new ListenerContainer();
    private ListenerContainer onReleaseListener    = new ListenerContainer();
    
    public void addOnClickListener( ActionListener listener )
    {
        this.onClickListener.addListener(listener);
    }
    
    public void removeOnClickListener( ActionListener listener )
    {
        this.onClickListener.removeListener(listener);
    }
    
    
    public void addOnEnterListener( ActionListener listener)
    {
        this.onEnterListener.addListener(listener);
    }
        
    public void removeOnEnterListener( ActionListener listener)
    {
        this.onEnterListener.removeListener(listener);
    }
    
    
    public void addOnLeaveListener( ActionListener listener )
    {
        this.onLeaveListener.addListener(listener);
    }
    
    public void removeOnLeaveListener( ActionListener listener)
    {
        this.onLeaveListener.removeListener(listener);
    }
    
    
    public void addOnPressListener( ActionListener listener )
    {
        this.onPressListener.addListener(listener);
    }
    
    public void removeOnPressListener( ActionListener listener)
    {
        this.onPressListener.removeListener(listener);
    }
    
    
    public void addOnReleaseListener( ActionListener listener)
    {
        this.onReleaseListener.addListener(listener);
    }
    
    public void removeOnReleaseListener( ActionListener listener)
    {
        this.onReleaseListener.removeListener(listener);
    }
    
    protected abstract void onClick();
    protected abstract void onMouseEnter();
    protected abstract void onMouseLeave();
    protected abstract void onPress();
    protected abstract void onRelease();
    
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
    
    protected boolean isMouseInArea()    { return inArea;         }
    protected boolean isLeftButtonHold() { return leftButtonHold; }
    
    private boolean inArea         = false;
    private boolean leftButtonHold = false;
    
    @Override
     final boolean onMouseMove( float x, float y )
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
        return false;
    }
    
    @Override
    final boolean onLeftMouseButtonDown( float x, float y) 
    {
        if( isContainPoint(x, y) )
        {
            leftButtonHold = true;
            onPressBase();
        }
        
        return false;
    }
    
    @Override
    final boolean onLeftMouseButtonUp( float x, float y) 
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
        return false;
    }

}

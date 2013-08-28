/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.layers;

/**
 *
 * @author vvv
 */
public abstract class BaseButton extends Widget
{
    private ActionListener onClickListener      = null;
    private ActionListener onEnterListener      = null;
    private ActionListener onLeaveListener      = null;
    private ActionListener onPressListener      = null;
    private ActionListener onReleaseListener    = null;
    
    public void addOnClickListener( ActionListener listener )
    {
        this.onClickListener = listener;
    }
    
    public void addOnEnterListener( ActionListener listener)
    {
        this.onEnterListener = listener;
    }
    
    public void addOnLeaveListener( ActionListener listener )
    {
        this.onLeaveListener = listener;
    }
    
    public void addOnPressListener( ActionListener listener )
    {
        this.onPressListener = listener;
    }
    
    public void addOnReleaseListener( ActionListener listener)
    {
        this.onReleaseListener = listener;
    }
    
    
    protected abstract void onClick();
    protected abstract void onMouseEnter();
    protected abstract void onMouseLeave();
    protected abstract void onPress();
    protected abstract void onRelease();
    
    private void onClickBase()
    {
        onClick();
        if( onClickListener != null )
        {
            onClickListener.action();
        }
    }
    
    private void onMouseEnterBase()
    {  
        onMouseEnter();
        if( onEnterListener != null )
        {
            onEnterListener.action();
        }
    }
    
    private void onMouseLeaveBase()
    {  
        onMouseLeave();
        if( onLeaveListener != null )
        {
            onLeaveListener.action();
        }
    }
    
 
    private void onPressBase()
    {
        onPress();
        if( onPressListener != null )
        {
            onPressListener.action();
        }
    }
    
    private void onReleaseBase()
    {
        onRelease();
        if( onReleaseListener != null )
        {
            onReleaseListener.action();
        }
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

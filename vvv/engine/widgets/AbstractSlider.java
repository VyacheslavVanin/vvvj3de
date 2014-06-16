/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vvv.engine.widgets;

/**
 *
 * @author vvv
 */
public abstract class AbstractSlider extends Widget
{
    private int range;
    private int value;

    private final ListenerContainer onDragListener = new ListenerContainer();
    
    public AbstractSlider(int range)
    {
        setRange( range );
        setValue( range/2 );
    }
    
    public AbstractSlider() 
    {
        this(100);
    }
    
    
    public final void setRange(int range)
    {
        if( range <= 0 )
        {
            throw new IllegalArgumentException("Range should be greater than zero");
        }
        
        this.range = range;
        setValue( range / 2);
    }
    
    
    public final void setValue( int value)
    {
                            // clamp value to [0..range]
        this.value = Math.max(0, Math.min(range, value) );
    }
    
    public final int getValue()
    {
        return this.value;
    }
    
    public final int getRange()
    {
        return this.range;
    }       

    /**
     * return value / range 
     * @return value / range
     */
    public final float getRelativeValue()
    {
        return value / (float)range;
    }
    
    public final void addOnDragListener( ActionListener listener)
    {
        this.onDragListener.addListener(listener);
    }
    
    public final void removeOnDragListener( ActionListener listener)
    {
        this.onDragListener.removeListener(listener);
    }
    
    
    
    protected void onMouseDrag(float x, float y) {}
    
    
    
    private void onDragBase(float x, float y)
    {
        onMouseDrag(x, y);
        onDragListener.action();
    }
    
    
    private boolean dragged = false;
    
    @Override
    protected void onMouseButtonDown(int button, float x, float y)
    {
        if( isContainPoint(x, y) && button == 0)
        {
            dragged = true;
        } 
    }
    
    @Override 
    protected void onMouseButtonUp( int button, float x, float y)
    {
	    if( dragged && button == 0)
        {
            dragged = false;
        }
    }
    
    @Override
    protected void onMouseMove( float x, float y)
    {
	    if( dragged )
        {
            onDragBase(x, y);
        }
    }
    
}

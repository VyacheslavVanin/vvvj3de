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
}

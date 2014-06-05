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
public class AbstractSlider 
{
    private int range;
    private int value;
    private int numTicks;

    public AbstractSlider(int range, int numTicks)
    {
        setRange(range);
        setNumTicks(numTicks);
        setValue( range/2 );
    }
    
    public AbstractSlider() 
    {
        this(100, 0);
    }
    
    public AbstractSlider(int range)
    {
        this(range,0);
    }
    
    public final void setRange(int range)
    {
        if( range <= 0 )
        {
            throw new IllegalArgumentException("Range should be greater than zero");
        }
        
        if(     ( this.numTicks > 0 ) 
             && ( range < numTicks-1) )
        {
            setNumTicks(range+1);
        }
        
        this.range = range;
    }
    
    public final void setNumTicks(int numTicks)
    {
        if( numTicks < 0 || numTicks > range + 1 )
        {
            throw new IllegalArgumentException( "numTicks should be ( 0 <= numTicks <= range+1)" );
        }
        this.numTicks = numTicks;
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
    
    public final int getTicks()
    {
        return this.numTicks;
    }
    
    
    
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.sprite;

import java.util.List;
import vvv.engine.texture.Texture;

/**
 *
 * @author Вячеслав
 *   Class contain frames of one animation, and return current frame in 
 *         current time
 */
public final class SpriteAnimation
{
    private final List<Texture> frames;
    private final long          duration;
    
    /**
     *  Constructor
     * @param list array of frames
     * @param duration  duration of animation loop 
     */
    public  SpriteAnimation( List<Texture> list, long duration)
    {
        if(list == null || list.isEmpty() )
        {
            throw new IllegalArgumentException( "list must not be empty" );
        }
        if(duration == 0)
        {
            throw new IllegalArgumentException( "duration must be non null" );
        }
        this.frames = list;
        this.duration = duration;
    }
     
    /**
     * Get current frame of animation 
     * @param begin start time of animation
     * @param now current time in milliseconds from epoch (System.currentTimeMillis())
     * @param speed play speed multiplier 
     * @param looped is animation Looped
     * @return  Texture corresponding to current time (now)
     */
    Texture getCurrent(long begin, long now, float speed, boolean looped)
    {    
        final int numFrames = frames.size();
        final long frameDuration = (long)(duration/speed) / numFrames;
        
        final int CurrentIndex = (int)( ( now - begin ) / frameDuration );
        if( looped )
        {
            return frames.get(CurrentIndex % numFrames);
        }
        else
        {
            if( CurrentIndex > numFrames - 1)
            {
                return frames.get(numFrames - 1);
            } 
        }
        return frames.get(CurrentIndex);
    }
}

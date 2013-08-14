/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.sprite;

import java.util.ArrayList;
import java.util.List;
import vvv.engine.texture.Texture;

/**
 *
 * @author Вячеслав
 * @brief  Class contain frames of one animation, and return current frame in 
 *         current time
 */
public class SpriteAnimation
{
    private List<Texture> frames;
    private long          duration;
    
    /**
     * @brief Constructor
     * @param list array of frames
     * @param duration  duration of animation loop 
     */
    public  SpriteAnimation( ArrayList<Texture> list, long duration)
    {
        if(list == null)
        {
            throw new IllegalArgumentException( "list must be non null" );
        }
        this.frames = list;
        this.duration = duration;
    }
     
    /**
     * 
     * @param begin start time of animation
     * @param now current time in milliseconds srom epoch (System.currentTimeMillis())
     * @param speed play speed multiplier 
     * @param looped is animation Looped
     * @return  Texture corresponding to current time (now)
     */
    public Texture getCurrent(long begin, long now, float speed, boolean looped)
    {    
        int numFrames = frames.size();
        long frameDuration = (long)(duration/speed) / numFrames;
        
        int CurrentIndex = (int)( ( now - begin ) / frameDuration );
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

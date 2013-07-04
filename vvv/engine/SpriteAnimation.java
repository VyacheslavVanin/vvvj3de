/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.util.ArrayList;
import java.util.List;

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
    private long          startTime;
    
    /**
     * @brief Constructor
     * @param list array of frames
     * @param duration  duration of animation loop 
     */
    public void SpriteAnimation( ArrayList<Texture> list, long duration)
    {
        if(list == null)
        {
            throw new IllegalArgumentException( "list must be non null" );
        }
        this.frames = list;
        this.duration = duration;
        this.startTime = 0;
    }
    
    /**
     * @brief setting begin time of loop
     * @param startTime time 
     */
    public void setStartTime( long startTime)
    {
        this.startTime = startTime;
    }
    
    /**
     * 
     * @param now current time in milliseconds srom epoch (System.currentTimeMillis())
     * @return Texture corresponding to current time (now)
     */
    public Texture getCurrent(long now)
    {    
        int numFrames = frames.size();
        long frameDuration = duration / numFrames;
        int CurrentIndex = (int) ((( now - startTime ) / frameDuration) % numFrames) ;
        return frames.get(CurrentIndex);
    }
}

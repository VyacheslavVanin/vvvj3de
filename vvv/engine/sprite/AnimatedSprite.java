/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.sprite;

import vvv.engine.texture.Texture;

/**
 *
 * @author Вячеслав
 */
public class AnimatedSprite extends Sprite
{
    static private enum ANIMATION_STATE
    {
        PLAY,
        PAUSED
    }
    
    private SpriteAnimation animation = null;
    private long            animationStartTime = 0;
    private long            animationPauseTime = 0;
    private float           animationSpeed = 1.0f;
    private boolean         looped = true;
    private ANIMATION_STATE state = ANIMATION_STATE.PAUSED;

    public void setAnimation( SpriteAnimation animation)
    {
        this.animation = animation;
    }
 
    public void setAnimationSpeed( float speed)
    {
        this.animationSpeed = speed;
    }
       
    public void  playAnimation()
    {
        state = ANIMATION_STATE.PLAY;
        animationStartTime = System.currentTimeMillis();
    }
    
    public void  pauseAnimation()
    {
        if( state != ANIMATION_STATE.PAUSED )
        {
            animationPauseTime = System.currentTimeMillis();
            state = ANIMATION_STATE.PAUSED;
        }
    }
    
    public void resetAnimation()
    {
        animationStartTime = System.currentTimeMillis();
        animationPauseTime = animationStartTime;
    }
    
    public void setPlayLooped( boolean looped)
    {
        this.looped = looped;
    }
    
    @Override
    public Texture getTexture(long millis)
    {
        if( animation == null )
        {
            throw new NullPointerException("Animation is not set");
        }
        
        if( state == ANIMATION_STATE.PLAY )
        {
            return  animation.getCurrent(animationStartTime, millis, 
                                         animationSpeed, looped);
        }
        else
        {
            return animation.getCurrent(animationStartTime, animationPauseTime, 
                                         animationSpeed, looped);
        }
    }
    
}

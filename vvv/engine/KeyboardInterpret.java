/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vvv.engine;

import org.lwjgl.input.Keyboard;

/**
 *
 * @author vvv
 */
public abstract class KeyboardInterpret 
{
    
    private long beginRepeatDelay = 500;
    private long repeatDelay = 20;
    
    private long pressAtTime = Long.MAX_VALUE;
    private long lastRepeatedPress = 0;
    
    private boolean repeat = false;
    
    private char repeatChar = 0;
    private int  repeatKey  = 0;
    
    
    public final void process()
    {
        
        while( Keyboard.next() )
        {
            final char c = Keyboard.getEventCharacter();
            final int  k = Keyboard.getEventKey();
            final boolean down = Keyboard.getEventKeyState();
            
            raw( k, down, c);
            if( down )
            {
                repeat = true;
                repeatChar = c;
                repeatKey = k;
                pressAtTime = System.currentTimeMillis();
                controlled( k, c );
            }
            else
            {
                if( k == repeatKey )
                {
                    repeat = false;
                }
            }
        }
        
        if(repeat)
        {
            final long currentNanos = System.currentTimeMillis();
            final long dt = currentNanos - pressAtTime;
            if( dt > getBeginRepeatDelay() )
            {
                if( currentNanos - lastRepeatedPress > getRepeatDelay() )
                {
                    controlled(repeatKey, repeatChar);
                    lastRepeatedPress = currentNanos;
                }
            }
        }
        
    }
    
    /**
     *  Called when keyboard event occurs with data as it is
     * @param key - scan code of key
     * @param state - true if pressed, false if released
     * @param character - character with current keyboard layout, language and so on */
    public abstract void raw( int key, boolean state, char character);
    
    /**
     *  Called as raw but may also called in such cases as repeat keypress when hold key for a while
     * @param key
     * @param character */
    public abstract void controlled(int key, char character);

    /**
     * @return the beginRepeatDelay
     */
    public long getBeginRepeatDelay() {
        return beginRepeatDelay;
    }

    /**
     * @param beginRepeatDelay the beginRepeatDelay to set
     */
    public void setBeginRepeatDelay(long beginRepeatDelay) {
        this.beginRepeatDelay = beginRepeatDelay;
    }

    /**
     * @return the repeatDelay
     */
    public long getRepeatDelay() {
        return repeatDelay;
    }

    /**
     * @param repeatDelay the repeatDelay to set
     */
    public void setRepeatDelay(long repeatDelay) {
        this.repeatDelay = repeatDelay;
    }
    
}

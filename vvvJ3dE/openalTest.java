/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvvJ3dE;

import java.nio.ShortBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import org.lwjgl.openal.ALCcontext;
import org.lwjgl.openal.ALCdevice;


/**
 *
 * @author Вячеслав
 */
public class openalTest
{
    private ALCdevice dev;
    private ALCcontext ctx;
    private int buf;
    
    public void init()
    {
         try
        {
            AL.create();
        }
        catch (LWJGLException ex)
        {
            Logger.getLogger(openalTest.class.getName()).log(Level.SEVERE, null,
                                                             ex);
        }
        String devname = alcGetString(null, ALC_DEFAULT_DEVICE_SPECIFIER);
        System.out.println(devname);
        dev = alcOpenDevice(devname);
        ctx = alcCreateContext( dev, null);
        alcMakeContextCurrent(ctx);
    }
    
    public void destroy()
    {
        ctx = alcGetCurrentContext();
        dev = alcGetContextsDevice(ctx);

        alcMakeContextCurrent(null);
        alcDestroyContext(ctx);
 
        alcCloseDevice(dev);
        AL.destroy();       
    }
    float seconds = 1.1f;
    public void initSound()
    {
        buf = alGenBuffers();
        
        float freq = 440;
        int   sample_rate = 22050;
        int bufsuze = (int)seconds * sample_rate;
        
        short samples[] = new short[bufsuze];
        for(int i = 0; i < samples.length; ++i)
        {
           samples[i] = (short) (32760.0 * Math.sin( 2*Math.PI * freq / sample_rate * i) /3)      ;
           samples[i] += (short) (32760.0/2 * Math.sin( 2*Math.PI * freq * 7 / sample_rate * i))/3;
           samples[i] += (short) (32760.0/3 * Math.sin( 2*Math.PI * freq * 19 / sample_rate * i))/3;
        }
        
   
        
        ShortBuffer sb = BufferUtils.createShortBuffer(bufsuze);
        sb.put(samples);
        sb.position(0);
        
        alBufferData(buf, AL_FORMAT_MONO16, sb, sample_rate);      
    }
    
    public void play()
    {
        int src = alGenSources();
        alSourcei(src, AL_BUFFER, buf);
        alSourcePlay(src);
        try
        {
            Thread.sleep( (long)(seconds*1001));
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(openalTest.class.getName()).log(Level.SEVERE, null,
                                                   ex);
        }
        alSourceStop(src);
        alDeleteSources(src);
        alDeleteBuffers(buf);
    }
    
    public void run()
    {
        init();
        initSound();
        play();
        destroy();
    }
    
    
    
    public static void main(String[] args)
    {
        openalTest m = new openalTest();
        m.run();
         
    }
}

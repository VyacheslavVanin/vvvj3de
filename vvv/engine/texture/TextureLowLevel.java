/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.texture;

import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static vvv.engine.Constants.*;

/**
 *
 * @author Вячеслав
 */
public class TextureLowLevel
{
    /**
     * TODO: сделать раздельные переменные для статуса онхост ондевайс
     */
    public boolean activate(int textureUnit) throws TextureNotLoadedException
    {
        if ((textureUnit < 0)
            || (textureUnit > MAX_TEXTURE_UNIT_NUMBER))
        {
            throw new IllegalArgumentException(
                "textureUnitNumber put of range (0..31)");
        }

        switch (status)
        {
            case EMPTY:
                throw new TextureNotLoadedException();
            case ON_HOST:
                hostToDevice();  // no break; cause we have to glBindTexture
            case ON_DEVICE:
                if (activeTextures[textureUnit] != tex)
                {
                    activeTextures[textureUnit] = tex;
                    glActiveTexture(GL_TEXTURE0 + textureUnit);
                    glBindTexture(GL_TEXTURE_2D, tex);
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean checkLoadToHostArgs(ByteBuffer buffer, int width, int height,
                                        int imageFormat, int textureFormat)
    {
        if (buffer == null)
        {
            return false;
        }

        if (buffer.capacity() == 0)
        {
            return false;
        }

        if (width < 1 || width > TEXTURE_MAX_SIZE
            || height < 1 || height > TEXTURE_MAX_SIZE)
        {
            return false;
        }

        return true;
    }

  

    public boolean loadToHost(ByteBuffer buffer, int width, int height,
                              int imageFormat, int textureFormat)
    {
        if (checkLoadToHostArgs(buffer, width, height,
                                imageFormat, textureFormat))
        {
            return false;
        }

        clearDevice();
        this.buffer = buffer;
        this.height = height;
        this.width = width;
        this.textureFormat = textureFormat;
        this.imageFormat = imageFormat;
        this.status = STATUS.ON_HOST;
        return true;
    }

    static private int imageFormatToGLInt(ImageFormat imageFormat)
    {
        int ret = GL_RGB;
        switch (imageFormat)
        {
            case GL_RED:
                ret = GL_RED;
                break;
            case GL_RG:
                ret = GL_RG;
                break;
            case GL_RGB:
                ret = GL_RGB;
                break;
            case GL_BGR:
                ret = GL_BGR;
                break;
            case GL_RGBA:
                ret = GL_RGBA;
                break;
            case GL_BGRA:
                ret = GL_BGRA;
                break;
        }
        return ret;
    }

    static private int textureFormatToGLInt(InternalFormat textureFormat)
    {
        int ret = 0;
        switch (textureFormat)
        {
            case GL_DEPTH_COMPONENT:
                ret = GL_DEPTH_COMPONENT;
                break;
            case GL_DEPTH_STENCIL:
                ret = GL_DEPTH_STENCIL;
                break;
            case GL_RED:
                ret = GL_RED;
                break;
            case GL_RG:
                ret = GL_RG;
                break;
            case GL_RGB:
                ret = GL_RGB;
                break;
            case GL_RGBA:
                ret = GL_RGBA;
                break;
        }
        return ret;
    }

    public boolean loadToHost(ByteBuffer buffer, int width, int height,
                              ImageFormat imageFormat,
                              InternalFormat textureFormat)
    {
        if (!checkLoadToHostArgs(buffer, width, height,
                                0, 0))
        {
            return false;
        }

        clearDevice();
        this.buffer = buffer;
        this.height = height;
        this.width = width;
        this.status = STATUS.ON_HOST;
        this.imageFormat = imageFormatToGLInt(imageFormat);
        this.textureFormat = textureFormatToGLInt(textureFormat);

        return true;
    }

    public boolean loadToHost( Image image, InternalFormat textureFormat)
    {
        clearDevice();
        this.buffer = image.getByteBuffer();
        this.height = (int) image.getHeight();
        this.width  = (int) image.getWidth();
        this.status = STATUS.ON_HOST;
        int nn = image.getNumChanels();
        switch(nn)
        {
            case 1: 
                this.imageFormat = GL_RED;
                break;
            case 2:
                this.imageFormat = GL_RG;
                break;
            case 3:
                this.imageFormat = GL_RGB;
                break;
            case 4:
                this.imageFormat = imageFormatToGLInt(ImageFormat.GL_RGBA);
                break;
        }
       
        this.textureFormat = textureFormatToGLInt(textureFormat);
        return true; 
    }
    
    
    public void hostToDevice() throws TextureNotLoadedException
    {
        if (status == STATUS.EMPTY)
        {
            throw new TextureNotLoadedException();
        }

        clearDevice();
        tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);

        glTexImage2D(GL_TEXTURE_2D, 0, this.textureFormat,
                     this.width, this.height, 0,
                     this.imageFormat, GL_UNSIGNED_BYTE, this.buffer);

        if (generateMipMap)
        {
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        
        if( wrapping != -1)
        {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapping);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapping);
        }
        
        if(this.minFilter != -1 )
        {
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, this.minFilter);
        }
        
        if(this.magFilter != -1)
        {
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, this.magFilter);
        }
        
        this.status = STATUS.ON_DEVICE;
    }

    static private int minFilterToGLInt(MINFILTER filter)
    {
        int ret = GL_NEAREST;
        switch (filter)
        {
            case LINEAR:
                ret = GL_LINEAR;
                break;
            case NEAREST:
                ret = GL_NEAREST;
                break;
            case LINEAR_MIPMAP_LINEAR:
                ret = GL_LINEAR_MIPMAP_LINEAR;
                break;
            case LINEAR_MIPMAP_NEAREST:
                ret = GL_LINEAR_MIPMAP_NEAREST;
                break;
            case NEAREST_MIPMAP_LINEAR:
                ret = GL_NEAREST_MIPMAP_LINEAR;
                break;
            case NEAREST_MIPMAP_NEAREST:
                ret = GL_NEAREST_MIPMAP_NEAREST;
                break;
        }
        return ret;
    }

    static private int magFilterToGLInt(MAGFILTER filter)
    {
        int ret = GL_LINEAR;
        switch (filter)
        {
            case LINEAR:
                ret = GL_LINEAR;
                break;
            case NEAREST:
                ret = GL_NEAREST;
                break;
            default:
                break;
        }
        return ret;
    }

    public void setFilter(MINFILTER minFilter, MAGFILTER magFilter) 
    {
        int glMinFilter = minFilterToGLInt(minFilter);
        int glMagFilter = magFilterToGLInt(magFilter);
        this.minFilter = glMinFilter;
        this.magFilter = glMagFilter;
        
        if( minFilter != MINFILTER.LINEAR || 
            minFilter != MINFILTER.NEAREST)
        {
            generateMipMap = true;
        }
         
        if (tex == -1)
        {    
            return;
        }
         
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, this.minFilter);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, this.magFilter);
    }

    static private int wrapToGLInt(WRAP wrap)
    {
        int ret = GL_REPEAT;
        switch (wrap)
        {
            case CLAMP_TO_BORDER:
                ret = GL_CLAMP_TO_BORDER;
                break;
            case CLAMP_TO_EDGE:
                ret = GL_CLAMP_TO_EDGE;
                break;
            case REPEAT:
                ret = GL_REPEAT;
                break;
            case MIRRORED_REPEAT:
                ret = GL_MIRRORED_REPEAT;
                break;
        }
        return ret;
    }

    public void setWraping(WRAP wrap)
    {
        int glwrap = wrapToGLInt(wrap);
        wrapping = glwrap;
        if (tex == -1)
        { 
            return;
        }
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapping);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapping);
    }

    public void setGenerateMipMap(boolean gen)
    {
        this.generateMipMap = gen;
    }

    public void clearHost()
    {
        clearDevice();
        this.buffer = null;
        this.generateMipMap = false;
        status = STATUS.EMPTY;
        imageFormat = 0;
        textureFormat = 0;
        height = 0;
        width = 0;
    }

    public void clearDevice()
    {
        if ((status == STATUS.ON_DEVICE) && (tex != -1))
        {
            glDeleteTextures(tex);
            tex = -1;
            status = STATUS.ON_HOST;
        }
    }
    
    private int tex = -1;
    private ByteBuffer buffer = null;
    private int imageFormat = 0;
    private int textureFormat = 0;
    private int height = 0;
    private int width = 0;
    private STATUS status = STATUS.EMPTY;
    private boolean generateMipMap = false;
    private int    wrapping = -1;
    private int    minFilter = -1;
    private int    magFilter = -1;
    private static int[] activeTextures;

    static
    {
        activeTextures = new int[NUM_TEXTURE_UNITS];
        for (int i = 0; i < NUM_TEXTURE_UNITS; ++i)
        {
            activeTextures[i] = -1;
        }
    }

    static public InternalFormat numChannelsToInternalFormat(int n)
    {
        InternalFormat ret;
        switch(n)
        {
            case 1:
                ret = InternalFormat.GL_RED;
                break;
            case 2:
                ret = InternalFormat.GL_RG;
                break;
            case 3:
                ret = InternalFormat.GL_RGB;
                break;
            case 4:
                ret = InternalFormat.GL_RGBA;
                break;
            default:
                ret = InternalFormat.GL_RGBA;
                break;
        }
        return ret;
    }
    
    static public ImageFormat numChannelsToImageFormat(int n)
    {
        ImageFormat ret;
        switch(n)
        {
            case 1:
                ret = ImageFormat.GL_RED;
                break;
            case 2:
                ret = ImageFormat.GL_RG;
                break;
            case 3:
                ret = ImageFormat.GL_RGB;
                break;
            case 4:
                ret = ImageFormat.GL_RGBA;
                break;
            default:
                throw new IllegalArgumentException("n should be 1..4");
        }
        return ret;
    }
    
    
    public static enum ImageFormat
    {
        GL_RED,
        GL_RG,
        GL_RGB,
        GL_BGR,
        GL_RGBA,
        GL_BGRA
    }

    public static enum InternalFormat
    {
        GL_DEPTH_COMPONENT,
        GL_DEPTH_STENCIL,
        GL_RED,
        GL_RG,
        GL_RGB,
        GL_RGBA
    }

    public static enum STATUS
    {
        EMPTY,
        ON_HOST,
        ON_DEVICE
    }

    public static enum WRAP
    {
        CLAMP_TO_EDGE,
        CLAMP_TO_BORDER,
        REPEAT,
        MIRRORED_REPEAT
    }

    public static enum MINFILTER
    {
        LINEAR,
        NEAREST,
        NEAREST_MIPMAP_NEAREST,
        LINEAR_MIPMAP_NEAREST,
        NEAREST_MIPMAP_LINEAR,
        LINEAR_MIPMAP_LINEAR
    }
    
    public static enum MAGFILTER
    {
        LINEAR,
        NEAREST
    }
    

    public static class TextureNotLoadedException extends Exception
    {
    }

    public static class TextureNotOnDeviceException extends Exception
    {
    }
}

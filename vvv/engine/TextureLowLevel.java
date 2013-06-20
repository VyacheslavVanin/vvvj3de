/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static vvv.engine.Consatants.*;
/**
 *
 * @author Вячеслав
 */
public class TextureLowLevel
{
    public void activate(int textureUnit) throws TextureNotLoadedException
    {
        if(    (textureUnit < 0) 
            || (textureUnit > MAX_TEXTURE_UNIT_NUMBER) )
        {
            throw new IllegalArgumentException("textureUnitNumber put of range (0..31)");
        }
        
        switch( status )
        {
            case EMPTY: 
                throw new TextureNotLoadedException();
            case ON_HOST: 
                hostToDevice();  // no break; cause we have to glBindTexture
            case ON_DEVICE:  
                if( activeTextures[textureUnit] != tex )
                {
                    activeTextures[textureUnit] = tex;
                    glActiveTexture( GL_TEXTURE0 + textureUnit );
                    glBindTexture( GL_TEXTURE_2D, tex);
                }
                break;
        }
    }
     
    private boolean checkLoadToHostArgs(ByteBuffer buffer, int width, int height, 
                                        int imageFormat,   int textureFormat)
    {
        if( buffer == null)
        {
            return false;
        }
        
        if( buffer.capacity() == 0)
        {
            return false;
        }
        
        if( width < 1 || width > TEXTURE_MAX_SIZE ||
            height < 1 || height > TEXTURE_MAX_SIZE )
        {
            return false;
        }
          
        return true;
    }
    
    public static int getNumComponentsInImage( BufferedImage im)
    {
        return im.getData().getNumDataElements();
    }
    
    public static ByteBuffer getByteBufferFromImage( BufferedImage im)
    {
        /** Platform dependent function  */
        int[] pdata;
        int iheight = im.getHeight();
        int iwidth  = im.getWidth();
        int nn = getNumComponentsInImage(im);
        pdata  = new int[im.getData().getHeight() * im.getData().getWidth()* nn ]; 
        im.getData().getPixels(0, 0, iwidth, iheight, pdata);

        ByteBuffer bb = ByteBuffer.allocateDirect(  nn * iheight * iwidth);
        for( int i=0 ; i < pdata.length / nn; ++i )
        {
            int dx = i % iwidth;
            int dy = i / iwidth;
            int p = (iheight - dy-1) * iwidth + dx;
            
            for( int j = 0; j < nn; ++j)
            {
               bb.put(  (byte)pdata[ p*nn + j ] );
            }
        }
        bb.position(0);
        return bb;
    }
    
    public static ByteBuffer getByteBufferFromImageFile( File file) throws IOException
    {
        /** Platform dependent function  */
        BufferedImage im = ImageIO.read( new File("texture.png") );
        return getByteBufferFromImage(im);
    }
    
    
    public boolean loadToHost( ByteBuffer buffer, int width, int height, 
                         int imageFormat, int textureFormat)
    {
        if(  checkLoadToHostArgs(buffer, width, height, 
                                imageFormat, textureFormat))
        {
            return false;
        }
        
        clearDevice();
        this.buffer = buffer;
        this.height = height;
        this.width  = width;
        this.textureFormat = textureFormat;
        this.imageFormat   = imageFormat;
        this.status = STATUS.ON_HOST;
        return true;
    }
      
    public boolean loadToHost( ByteBuffer buffer, int width, int height, 
                         ImageFormat imageFormat, InternalFormat textureFormat)
    {
        if(  checkLoadToHostArgs(buffer, width, height, 
                                0, 0))
        {
            return false;
        }
        
        clearDevice();
        this.buffer = buffer;
        this.height = height;
        this.width  = width;
        this.status = STATUS.ON_HOST;
        
        switch(imageFormat)
        {
            case GL_RED:
                this.imageFormat = GL_RED;
                break;
            case GL_RG:
                this.imageFormat = GL_RG;
                break;
            case GL_RGB:
                this.imageFormat = GL_RGB;
                break;
            case GL_BGR:
                this.imageFormat = GL_BGR;
                break;
            case GL_RGBA:
                this.imageFormat = GL_RGBA;
                break;
            case GL_BGRA:
                this.imageFormat = GL_BGRA;
                break;
        }
        
        switch( textureFormat )
        {
            case GL_DEPTH_COMPONENT:
                this.textureFormat = GL_DEPTH_COMPONENT;
                break;
            case GL_DEPTH_STENCIL:
                this.textureFormat = GL_DEPTH_STENCIL;
                break;
            case GL_RED:
                this.textureFormat = GL_RED;
                break;
            case GL_RG:
                this.textureFormat = GL_RG;
                break;
            case GL_RGB:
                this.textureFormat = GL_RGB;
                break;
            case GL_RGBA:
                this.textureFormat = GL_RGBA;
                break;
        }
        return true;
    }
    
    
    public void hostToDevice() throws TextureNotLoadedException 
    {
        if( status == STATUS.EMPTY )
        {
            throw new TextureNotLoadedException();
        }
          
        clearDevice();
        tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
 
        glTexImage2D(GL_TEXTURE_2D, 0, this.textureFormat, 
                     this.width, this.height, 0, 
		     this.imageFormat, GL_UNSIGNED_BYTE, this.buffer);
          
        if( generateMipMap )
        {
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        
        this.status = STATUS.ON_DEVICE;
    }
    
    
    public void setFilter(FILTER minFilter, FILTER magFilter) throws TextureNotOnDeviceException
    {
        if( tex == -1 )
        {
            throw new TextureNotOnDeviceException();
        }
        
        glBindTexture(GL_TEXTURE_2D, tex);
     
        int glMinFilter = GL_NEAREST;
        switch( minFilter)
        {
            case LINEAR:
                glMinFilter = GL_LINEAR;
                break;
            case NEAREST:
                glMinFilter = GL_NEAREST;
                break;
            case LINEAR_MIPMAP_LINEAR:
                glMinFilter = GL_LINEAR_MIPMAP_LINEAR;
                break;
            case LINEAR_MIPMAP_NEAREST:
                glMinFilter = GL_LINEAR_MIPMAP_NEAREST;
                break;
            case NEAREST_MIPMAP_LINEAR:
                glMinFilter = GL_NEAREST_MIPMAP_LINEAR;
                break;
            case NEAREST_MIPMAP_NEAREST:
                glMinFilter = GL_NEAREST_MIPMAP_NEAREST;
                break;
        }
        
        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, glMinFilter );
        
        int glMagFilter = GL_LINEAR;
        switch( magFilter )
        {
            case LINEAR:
                glMagFilter = GL_LINEAR;
                break;
            case NEAREST:
                glMagFilter = GL_NEAREST;
                break;
            default:
                break;
        }  
        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, glMagFilter );
    }
    
    public void setWraping( WRAP wrap ) throws TextureNotOnDeviceException
    {
        if( tex == -1 )
        {
            throw new TextureNotOnDeviceException();
        }
        
        glBindTexture(GL_TEXTURE_2D, tex);
        
        int glwrap = GL_REPEAT;
        switch( wrap )
        {
            case CLAMP_TO_BORDER:
                glwrap = GL_CLAMP_TO_BORDER;
                break;
            case CLAMP_TO_EDGE:
                glwrap = GL_CLAMP_TO_EDGE;
                break;
            case REPEAT:
                glwrap = GL_REPEAT;
                break;
            case MIRRORED_REPEAT:
                glwrap = GL_MIRRORED_REPEAT;
                break;
        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, glwrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, glwrap);
    }
    
    public void setGenerateMipMap( boolean gen)
    {
        this.generateMipMap = gen;
    }
    
    
    public void clearHost() 
    {
        clearDevice();
        this.buffer = null;
        this.generateMipMap = true;
        status = STATUS.EMPTY;
        imageFormat   = 0;
        textureFormat = 0;
        height = 0;
        width  = 0;     
    }
    
    public void clearDevice() 
    {
        if( (status == STATUS.ON_DEVICE) && (tex != -1) )
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
    private STATUS  status = STATUS.EMPTY;
    private boolean generateMipMap = true;
    
    private static int[] activeTextures;
    static
    {
        activeTextures= new int[ NUM_TEXTURE_UNITS ];
        for( int i=0; i < NUM_TEXTURE_UNITS; ++i )
        {
            activeTextures[i] = -1;
        }
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
    
    public static enum FILTER
    {
        LINEAR,
        NEAREST,
        NEAREST_MIPMAP_NEAREST,
        LINEAR_MIPMAP_NEAREST,
        NEAREST_MIPMAP_LINEAR,
        LINEAR_MIPMAP_LINEAR
    }
    
    public static class TextureNotLoadedException extends Exception
    {     
    }

    public static class TextureNotOnDeviceException extends Exception
    {     
    }   
}



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.awt.Desktop;
import java.io.IOException;
import vvv.engine.text.Font;
import vvv.engine.texture.Texture;
import vvv.engine.texture.TextureLoader;
import vvv.engine.texture.TextureLowLevel;

/**
 *
 * @author QwertyVVV
 */
public class Defaults
{

    static private volatile Texture defaultTexture = null;
    static private volatile Font defaulFont = null;
    static private final Object   textureLock = new Object();
    static private final Object   fontLock    = new Object();
    
    
    static public void setDefaultFont( Font font)
    {
        defaulFont = font;
    }
    
    static public void setDefaultTexture( Texture texture )
    {
        defaultTexture = texture;
    }
    
    static private void initTexture() throws IOException
    {
        defaultTexture = TextureLoader.loadFromFile(Constants.DEFAULTS_DIRECTORY + 
                                                    Constants.DEFAULT_TEXTURE_NAME,
                                                    TextureLowLevel.InternalFormat.GL_RGBA);
    }

    static private void initFont() throws IOException
    {
        defaulFont = Font.loadFromFiles(Constants.DEFAULTS_DIRECTORY
                                         + Constants.DEFAULT_FONT_NAME);
    }

    static public Texture getTexture() throws IOException
    {
        if(defaultTexture == null)
        {
            synchronized(textureLock)
            {
                if(defaultTexture == null)
                {
                    initTexture();
                }
            }
        }
        return defaultTexture;
    }

    static public Font getFont() throws IOException
    {
        if(defaulFont == null )
        {
            synchronized( fontLock )
            {
                if( defaulFont == null )
                {
                    initFont();
                }
            }
        }
        return defaulFont;
    }
}

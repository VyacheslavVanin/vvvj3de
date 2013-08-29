/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.io.IOException;
import vvv.engine.text.Font;
import vvv.engine.texture.Texture;
import vvv.engine.texture.TextureContainer;
import vvv.engine.texture.TextureLoader;
import vvv.engine.texture.TextureLowLevel;

/**
 *
 * @author QwertyVVV
 */
public class Defaults
{
    private static final String DEFAULTS_DIRECTORY = "defaults/";
    private static final String DEFAULT_TEXTURE_NAME = "defaultTexture.png";
    private static final String DEFAULT_FONT_NAME    = "defaultFont.png";
    
    private static final String DEFAULT_GUI_DIRECTORY = "gui/";
    private static final String GUI_CHECKBOX_CHECKED = "check_box_checked.png";
    private static final String GUI_CHECKBOX_UNCHECKED = "check_box_unchecked.png";
    
    static private volatile Texture defaultTexture = null;
    static private volatile Font defaulFont = null;
    static private final Object   textureLock = new Object();
    static private final Object   fontLock    = new Object();
    static private final Object   guiTexturesLock = new Object();
    static private volatile TextureContainer guiTextures = null;
    
    static public void setDefaultFont( Font font)
    {
        defaulFont = font;
    }
    
    static public void setDefaultTexture( Texture texture )
    {
        defaultTexture = texture;
    }
    
    static private void initGuiTextures() throws IOException
    {
        String dir = DEFAULTS_DIRECTORY + DEFAULT_GUI_DIRECTORY; 
        guiTextures = new TextureContainer();
        guiTextures.addTexture( dir + GUI_CHECKBOX_CHECKED, GUI_CHECKBOX_CHECKED );
        guiTextures.addTexture( dir + GUI_CHECKBOX_UNCHECKED, GUI_CHECKBOX_UNCHECKED);
        guiTextures.pack();
    }
    
    static private void initTexture() throws IOException
    {
        defaultTexture = TextureLoader.loadFromFile(DEFAULTS_DIRECTORY + 
                                                    DEFAULT_TEXTURE_NAME,
                                                    TextureLowLevel.InternalFormat.GL_RGBA);
    }

    static private void initFont() throws IOException
    {
        defaulFont = Font.loadFromFiles(DEFAULTS_DIRECTORY
                                         + DEFAULT_FONT_NAME);
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
    
    static private TextureContainer getGuiTextures() throws IOException
    {
        if( guiTextures == null )
        {
            synchronized( guiTexturesLock )
            {
                if( guiTextures == null )
                {
                    initGuiTextures();
                }
            }
        }
        return guiTextures;
    }
    
    static public Texture getCheckBoxCheckedTexture() throws IOException
    {
        return getGuiTextures().GetTexture(GUI_CHECKBOX_CHECKED);
    }
    
    static public Texture getCheckBoxUncheckedTexture() throws IOException
    {
        return getGuiTextures().GetTexture(GUI_CHECKBOX_UNCHECKED);
    }
    
}

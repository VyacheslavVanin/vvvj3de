/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.io.IOException;
import vvv.engine.Singletone;
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
    public static final String DEFAULTS_DIRECTORY = "defaults/";
    private static final String DEFAULT_TEXTURE_NAME = "defaultTexture.png";
    private static final String DEFAULT_FONT_NAME    = "defaultFont.png";
    

    static private Singletone<Texture> defaultTexture = 
            new Singletone<>(
                              new Singletone.SingletoneCreator<Texture>() 
    {
        @Override
        public Texture create() throws IOException
        {
            return TextureLoader.loadFromFile(DEFAULTS_DIRECTORY + 
                                                    DEFAULT_TEXTURE_NAME,
                                                    TextureLowLevel.InternalFormat.GL_RGBA);
        }
    });
    
    static private Singletone<Font> defaultFont = 
            new Singletone<>( 
                              new Singletone.SingletoneCreator<Font>() 
    {
        @Override
        public Font create() throws IOException
        {
            return Font.loadFromFiles(DEFAULTS_DIRECTORY
                                         + DEFAULT_FONT_NAME);
        }
    });
    
    
    static public void setDefaultFont( Font font)
    {
        defaultFont.set(font);    
    }
    
    static public void setDefaultTexture( Texture texture )
    {
        defaultTexture.set(texture);
    }
 

    static public Texture getTexture() throws IOException
    {
        return defaultTexture.get();
    }

    static public Font getFont() throws IOException
    {
        return defaultFont.get();
    }
 
}

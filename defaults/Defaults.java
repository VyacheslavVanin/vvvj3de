/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;
import vvv.engine.Singletone;
import vvv.engine.shader.ModelShader;
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
    public static final  String DEFAULTS_DIRECTORY   = "defaults/";
    private static final String DEFAULT_TEXTURE_NAME = "defaultTexture.png";
    private static final String DEFAULT_FONT_NAME    = "defaultFont.png";
    private static final String DEFAULT_SPRITE_VERTEX_SHADER_NAME = "defaults/shaders/sprite.vs";
    private static final String DEFAULT_SPRITE_FRAGMENT_SHADER_NAME = "defaults/shaders/sprite.fs";
    private static final String DEFAULT_TEXT_VERTEX_SHADER_NAME = "defaults/shaders/text.vs";
    private static final String DEFAULT_TEXT_FRAGMENT_SHADER_NAME = "defaults/shaders/text.fs";

    private static final String DEFAULT_SOLID_VERTEX_SHADER_NAME = "defaults/shaders/solidColor.vs";
    private static final String DEFAULT_SOLID_FRAGMENT_SHADER_NAME = "defaults/shaders/solidColor.fs";
    
    private static final Singletone<Texture> defaultTexture = 
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
    
    private static final Singletone<Font>    defaultFont = 
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
    
    private static final Singletone<ModelShader> spriteShader =
            new Singletone<>( new Singletone.SingletoneCreator<ModelShader>() 
    {
        @Override
        public ModelShader create() throws IOException
        {
            ModelShader ret = new ModelShader();
            ret.loadFromFiles(DEFAULT_SPRITE_VERTEX_SHADER_NAME, 
                              DEFAULT_SPRITE_FRAGMENT_SHADER_NAME);
            return ret;
        }
    });
    
    private static final Singletone<ModelShader> textShader =
            new Singletone<>( new Singletone.SingletoneCreator<ModelShader>() 
    {
        @Override
        public ModelShader create() throws IOException
        {
            ModelShader ret = new ModelShader();
            ret.loadFromFiles(DEFAULT_TEXT_VERTEX_SHADER_NAME, 
                              DEFAULT_TEXT_FRAGMENT_SHADER_NAME);
            return ret;
        }
    });
    
    private static final Singletone<ModelShader> solidColorShader = 
	    new Singletone<>( new Singletone.SingletoneCreator<ModelShader>() {

	    @Override
	    public ModelShader create() throws IOException 
	    {
		ModelShader ret = new ModelShader();
		ret.loadFromFiles(DEFAULT_SOLID_VERTEX_SHADER_NAME, 
                              DEFAULT_SOLID_FRAGMENT_SHADER_NAME);
		return ret;
	    }
    } );
    
    public static void setDefaultFont( Font font)
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
 
    
    static public void enableTransparency()
    {
        glEnable( GL_BLEND );
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
    
    static public void disableTransparency()
    {
        glDisable(GL_BLEND);
    }
    
    static public ModelShader getSpriteShader() throws IOException
    {
        return spriteShader.get();
    }
    
    static public ModelShader getTextShader() throws IOException
    {
        return textShader.get();
    }
    
    static public ModelShader getSolidColorShader() throws IOException
    {
	return solidColorShader.get();
    }
    
}

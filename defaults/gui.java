/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.io.IOException;
import vvv.engine.Singletone;
import vvv.engine.texture.Texture;
import vvv.engine.texture.TextureContainer;

/**
 *
 * @author Вячеслав
 */
public class gui
{  
    private static final String DEFAULT_GUI_DIRECTORY = "gui/";
    private static final String DEFAULT_GUI_SHADER_DIRECTORY = DEFAULT_GUI_DIRECTORY + "shaders/";
    private static final String GUI_CHECKBOX_CHECKED = "check_box_checked.png";
    private static final String GUI_CHECKBOX_UNCHECKED = "check_box_unchecked.png";
    private static final String GUI_PANEL    = "panel_texture.png";
    private static final String GUI_TRANSPARENT_COLOR_FRAG_SHADER = "transparent_color.fsh";
    private static final String GUI_TRANSPARENT_COLOR_VERTEX_SHADER = "transparent_color.vsh";
    
    private static Singletone<TextureContainer> guiTextures = new Singletone<>(new Singletone.SingletoneCreator<TextureContainer>() 
    {
        @Override
        public TextureContainer create() throws IOException
        {
            TextureContainer ret = new TextureContainer();
            String dir = Defaults.DEFAULTS_DIRECTORY + DEFAULT_GUI_DIRECTORY;
            ret.addTexture( dir + GUI_CHECKBOX_CHECKED, GUI_CHECKBOX_CHECKED );
            ret.addTexture( dir + GUI_CHECKBOX_UNCHECKED, GUI_CHECKBOX_UNCHECKED);
            ret.addTexture( dir + GUI_PANEL, GUI_PANEL );
            ret.pack(); 
            return ret;
        }
    });
    
    
    static private TextureContainer getGuiTextures() throws IOException
    {
        return guiTextures.get();
    }
    
    static public Texture getCheckBoxCheckedTexture() throws IOException
    {
        return getGuiTextures().GetTexture(GUI_CHECKBOX_CHECKED);
    }
    
    static public Texture getCheckBoxUncheckedTexture() throws IOException
    {
        return getGuiTextures().GetTexture(GUI_CHECKBOX_UNCHECKED);
    }
    
    static public Texture getPanelTexture() throws IOException
    {
        return getGuiTextures().GetTexture(GUI_PANEL);
    }
    
    
    
}

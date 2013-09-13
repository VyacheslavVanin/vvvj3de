/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.io.IOException;
import vvv.engine.Singletone;
import vvv.engine.shader.ModelShader;
import vvv.engine.texture.Texture;
import vvv.engine.texture.TextureContainer;

/**
 *
 * @author Вячеслав
 */
public class Gui
{  
    private static final String DEFAULT_GUI_DIRECTORY =  "gui/";
    private static final String DEFAULT_GUI_SHADER_DIRECTORY = DEFAULT_GUI_DIRECTORY + "shaders/";
    private static final String GUI_CHECKBOX_CHECKED = "check_box_checked.png";
    private static final String GUI_CHECKBOX_UNCHECKED = "check_box_unchecked.png";
    private static final String GUI_PANEL    = "panel_texture.png";
    private static final String GUI_BUTTON_CHECKED = "button_checked.png";
    private static final String GUI_BUTTON_UNCHECKED = "button_unchecked.png";
    
    private static final String GUI_TRANSPARENT_COLOR_FRAG_SHADER = "transparent_color.fs";
    private static final String GUI_TRANSPARENT_COLOR_VERTEX_SHADER = "transparent_color.vs";
    private static final String GUI_COLOR_MAP_FRAG_SHADER = "color_map.fs";
    private static final String GUI_COLOR_MAP_VERTEX_SHADER = "color_map.vs";
    
    
    private static Singletone<TextureContainer> guiTextures = 
            new Singletone<>(new Singletone.SingletoneCreator<TextureContainer>() 
    {
        @Override
        public TextureContainer create() throws IOException
        {
            TextureContainer ret = new TextureContainer();
            String dir = Defaults.DEFAULTS_DIRECTORY + DEFAULT_GUI_DIRECTORY;
            ret.addTexture( dir + GUI_CHECKBOX_CHECKED, GUI_CHECKBOX_CHECKED );
            ret.addTexture( dir + GUI_CHECKBOX_UNCHECKED, GUI_CHECKBOX_UNCHECKED);
            ret.addTexture( dir + GUI_PANEL, GUI_PANEL );
            ret.addTexture( dir + GUI_BUTTON_CHECKED, GUI_BUTTON_CHECKED );
            ret.addTexture( dir + GUI_BUTTON_UNCHECKED, GUI_BUTTON_UNCHECKED );
            ret.pack(); 
            return ret;
        }
    });
    
    private static Singletone<ModelShader> panelShader = 
            new Singletone<>( new Singletone.SingletoneCreator<ModelShader>() 
            {
                @Override
                public ModelShader create() throws IOException
                {
                    ModelShader ret = new ModelShader();
                    ret.loadFromFiles(Defaults.DEFAULTS_DIRECTORY +DEFAULT_GUI_SHADER_DIRECTORY + GUI_TRANSPARENT_COLOR_VERTEX_SHADER, 
                                      Defaults.DEFAULTS_DIRECTORY +DEFAULT_GUI_SHADER_DIRECTORY + GUI_TRANSPARENT_COLOR_FRAG_SHADER);
                    return ret;
                }
            });
    
    private static Singletone<ModelShader> colorMapShader = 
            new Singletone<>(new Singletone.SingletoneCreator<ModelShader>() {

        @Override
        public ModelShader create() throws IOException
        {
            ModelShader ret = new ModelShader();
                    ret.loadFromFiles(Defaults.DEFAULTS_DIRECTORY +DEFAULT_GUI_SHADER_DIRECTORY + GUI_COLOR_MAP_VERTEX_SHADER, 
                                      Defaults.DEFAULTS_DIRECTORY +DEFAULT_GUI_SHADER_DIRECTORY + GUI_COLOR_MAP_FRAG_SHADER);
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
    
    static public ModelShader getPanelShader() throws IOException
    {
        return panelShader.get();
    }
    
    static public Texture getButtonCheckedTexture() throws IOException
    {
        return getGuiTextures().GetTexture( GUI_BUTTON_CHECKED);
    }
    
    static public Texture getButtonUncheckedTexture() throws IOException
    {
        return getGuiTextures().GetTexture( GUI_BUTTON_UNCHECKED);
    }
    
    static public ModelShader getColorMapShader() throws IOException
    {
        return colorMapShader.get();
    }
    
    static public ModelShader getSpriteShader() throws IOException
    {
        return Defaults.getSpriteShader();
    }
}

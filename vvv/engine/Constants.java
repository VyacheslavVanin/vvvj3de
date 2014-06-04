/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
/**
 *
 * @author Вячеслав
 */
public class Constants
{
    private static int getMaxTextureUnits() 
    { 
        
        return glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS)/2;
    }
    private static int getMaxTextureSize()  
    { 
        return glGetInteger(GL_MAX_TEXTURE_SIZE)/4;
    }
    
    public static final int MAX_TEXTURE_UNIT_NUMBER = getMaxTextureUnits()-1;
    public static final int NUM_TEXTURE_UNITS = MAX_TEXTURE_UNIT_NUMBER+1;
    public static final int NUM_COLOR_UNIFORMS = 8;
    public static final int TEXTURE_MAX_SIZE = getMaxTextureSize();

}

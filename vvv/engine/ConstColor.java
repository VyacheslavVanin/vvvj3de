/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author QwertyVVV
 */
public class ConstColor implements Color
{
    private final Vector4f color = new Vector4f(1, 1, 1, 1);
       
    public ConstColor(float red, float green, float blue, float alpha ) 
    {
        color.set(red, green, blue, alpha);
    } 
              
    @Override
    public Vector4f getVector()
    {
        return color;
    }
    
    public static final ConstColor RED      = new ConstColor(1, 0, 0, 1);
    public static final ConstColor GREEN    = new ConstColor(0, 1, 0, 1);
    public static final ConstColor BLUE     = new ConstColor(0, 0, 1, 1);
    public static final ConstColor YELLOW   = new ConstColor(1, 1, 0, 1);
    public static final ConstColor WHITE    = new ConstColor(1, 1, 1, 1);
    public static final ConstColor BLACK    = new ConstColor(0, 0, 0, 1);
    public static final ConstColor GRAY     = new ConstColor(0.5f, 0.5f, 0.5f, 1);
    public static final ConstColor DARK_GRAY= new ConstColor(0.25f, 0.25f, 0.25f, 1);
    public static final ConstColor LIGHT_GRAY= new ConstColor(0.75f, 0.75f, 0.75f, 1);    
}

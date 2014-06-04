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
public class VarColor implements Color
{
    private final Vector4f color = new Vector4f(1, 1, 1, 1);
       
    public VarColor(float red, float green, float blue, float alpha ) 
    {
        color.set(red, green, blue, alpha);
    } 
              
    @Override
    public Vector4f getVector()
    {
        return color;
    } 
}

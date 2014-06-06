/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vvv.engine;

import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author vvv
 */
public final class VariableColor implements Color
{
    private final Vector4f color = new Vector4f(1, 1, 1, 1);
    
    public VariableColor(float red, float green, float blue, float alpha) 
    {
        color.set(red, green, blue, alpha);
    }
    
    public VariableColor(float red, float green, float blue)
    {
        color.set(red, green, blue, 1);
    }
    
    public VariableColor( Color c)
    {
        color.set(c.getVector());
    }
    
    public VariableColor()
    {
        this(1,1,1,1);
    }
    
    public void setColor(float red, float green, float blue, float alpha)
    {
        color.set(red, green, blue, alpha);
    }
    
    public void setColor(float red, float green, float blue)
    {
        color.set(red, green, blue, color.w);
    }
    
    public void setColor( Color c )
    {
        color.set( c.getVector() );
    }
    
    public void setRed(float red)
    {
        color.setX(red);
    }
    
    public void  setGreen(float green)
    {
        color.setY(green);
    }
    
    public void setBlue(float blue)
    {
        color.setZ(blue);
    }
    
    public void setAlpha(float alpha)
    {
        color.setW(alpha);
    }

    @Override
    public Vector4f getVector() 
    {
        return color;
    }
    
}

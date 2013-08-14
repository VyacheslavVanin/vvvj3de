/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.texture;

import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Вячеслав
 */
public class TexCoordData
{
    private Vector4f data = new Vector4f();

    public void setPos(float x, float y)
    {
        data.x = x;
        data.y = y;
    }

    public void setSize(float x, float y)
    {
        data.z = x;
        data.w = y;
    }

    public void set(float x, float y, float w, float h)
    {
        data.set(x, y, w, h);
    }

    public void set(final TexCoordData src)
    {
        data.set(src.data);
    }

    public Vector4f get()
    {
        return data;
    }
    
    public float getX()
    {
        return data.x;
    }
    
    public float getY()
    {
        return data.y;
    }
    
    public float getWidth()
    {
        return data.z;
    }
    
    public float getHeight()
    {
        return data.w;
    }
    
}

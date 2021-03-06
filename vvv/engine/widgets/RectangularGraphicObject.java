/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vvv.engine.widgets;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author vvv
 */
public class RectangularGraphicObject extends GraphicObject
{
    private final PositionProperties position = new PositionProperties();
    
    public RectangularGraphicObject()
    {
    }
    
    public Matrix4f getMatrix4f()
    {
        return position.getMatrix4f();
    }
    
    public void rotate(float angle, float x, float y, float z)
    {
        position.rotate(angle, x, y, z);
    }
    
    public void setRotation(float angle, float x, float y, float z)
    {
        position.setRotation(angle, x, y, z);
    }
    
    public void setEuler(float pitch, float yaw, float roll)
    {
        position.setEuler(pitch, yaw, roll);
    }
    
    public void setPosition(float x, float y, float z)
    {
        position.setPosition(x, y, z);
    }
    
    public void move(float x, float y, float z)
    {
        position.move(x, y, z);
    }
    
    public void setScale(float x, float y, float z)
    {
        position.setScale(x, y, z);
    }
    
    public void scaleMul( float x, float y, float z)
    {
        position.scaleMul(x, y, z);
    }
    
    public void scaleAdd( float x, float y, float z)
    {
        position.scaleAdd(x, y, z);
    }
      
    public Vector3f getPosition() { return position.getPosition();}
    public Vector3f getScale()    { return position.getScale();}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import vvv.math.Quat;
import vvv.math.Vec3;

/**
 *
 * @author Вячеслав
 */
public final class PositionProperties
{
    private final Vector3f position = new Vector3f(0,0,0);
    private final Vector3f scale    = new Vector3f(1,1,1); 
    private final Quat     angle    = new Quat();
    
    private final Matrix4f modelMatrix = new Matrix4f();
    
    private final Vec3 vAxis = new Vec3();
    private final Vector3f helpVector = new Vector3f();
    private boolean  changed = true;
    
    public PositionProperties()
    {
        setRotation(0, 0, 0, 1);
    }
    
     private void updateMatrix()
    {
        if(changed)
        {
            float a = angle.getAngle();
            angle.getAxis(vAxis);   

            helpVector.set( vAxis.x(), vAxis.y(), vAxis.z() );
            
            modelMatrix.setIdentity();
            modelMatrix.translate(position); 
            modelMatrix.rotate(a, helpVector);
            modelMatrix.scale(scale);

            changed = false;
        }
    }
    
    public Matrix4f getMatrix4f()
    {
        updateMatrix();
        return modelMatrix;
    }
    
    public void rotate(float angle, float x, float y, float z)
    {
        this.angle.rotate(angle, x, y, z);
        changed = true;
    }
    
    public void setRotation(float angle, float x, float y, float z)
    {
        this.angle.setAngleAxis(angle, x, y, z);
        changed = true;
    }
    
    public void setEuler(float pitch, float yaw, float roll)
    {
        this.angle.setEuler(pitch, yaw, roll);
        changed = true;
    }
    
    public void setPosition(float x, float y, float z)
    {
        this.position.set(x, y, z);
        changed = true;
    }
    
    public void move(float x, float y, float z)
    {
        this.setPosition( this.position.x + x, 
                          this.position.y + y, 
                          this.position.z + z );
    }
    
    public void setScale(float x, float y, float z)
    {
        this.scale.set(x, y, z);
        changed = true;
    }
    
    public void scaleMul( float x, float y, float z)
    {
        this.scale.x *= x;
        this.scale.y *= y;
        this.scale.z *= z;
        changed = true;
    }
    
    public void scaleAdd( float x, float y, float z)
    {
        this.scale.x += x;
        this.scale.y += y;
        this.scale.z += z;
        changed = true;
    }
    
    public Vector3f getPosition() { return position;}
    public Vector3f getScale()    { return scale;}
}

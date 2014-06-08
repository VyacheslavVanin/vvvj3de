/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.colorRectangle;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import vvv.engine.Geometry;

/**
 *
 * @author vvv
 */
public class RectangleGeometry {

    private static Geometry rectangleGeometry = null;
    
    private static void initGeometry()
    {
        rectangleGeometry = new Geometry();
        float[] vertices = 
        {
            -0.5f, -0.5f, 0, 
            -0.5f,  0.5f, 0,
             0.5f,  0.5f, 0, 
             0.5f, -0.5f, 0, 
        };
        
        int[] indices = 
        {
            0, 1, 3,
            1, 2, 3
        };
        
        
        ByteBuffer vbb = BufferUtils.createByteBuffer(vertices.length * 4);
        for (int i = 0; i < vertices.length; ++i)
        {
            vbb.putFloat(vertices[i]);
        }
        vbb.flip();

        ByteBuffer ibb = BufferUtils.createByteBuffer(indices.length * 4);
        for (int i = 0; i < indices.length; ++i)
        {
            ibb.putInt(indices[i]);
        }
        ibb.flip();

        
        Geometry.VertexAttribs attribs = new Geometry.VertexAttribs();
        attribs.add( Geometry.VertexAttribs.VERTEX_ATTRIBUTE.POSITION, 3, GL_FLOAT );
        
        rectangleGeometry.loadToHost(vbb, attribs, ibb, indices.length, GL_UNSIGNED_INT);
        
    }
    
    public static void reload()
    {
        if( rectangleGeometry != null )
        {     
            rectangleGeometry.hostToDevice();
        }
        else
        {
            initGeometry();
            rectangleGeometry.hostToDevice();
        }
    }

    public static Geometry getGeometry()
    {
        if( rectangleGeometry == null )
        {
            reload();
        }
        return rectangleGeometry;
    }
    
}

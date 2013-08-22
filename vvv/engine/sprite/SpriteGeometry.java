/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.sprite;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import vvv.engine.Geometry;

/**
 *
 * @author vvv
 */
public class SpriteGeometry
{
    static private Geometry spriteGeometry = null;
    
    static private void initGeometry()
    {
        spriteGeometry = new Geometry();
        float[] vertices =
        {
            -0.5f, -0.5f, 0, 0, 0,
            -0.5f, 0.5f, 0, 0, 1,
            0.5f, 0.5f, 0, 1, 1,
            0.5f, -0.5f, 0, 1, 0
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
        attribs.add( Geometry.VertexAttribs.VERTEX_ATTRIBUTE.TEXCOORD, 2, GL_FLOAT );
        
        spriteGeometry.loadToHost(vbb, attribs, ibb, indices.length, GL_UNSIGNED_INT);
    }

    static public void reload()
    {
        if( spriteGeometry != null )
        {     
            spriteGeometry.hostToDevice();
        }
        else
        {
            initGeometry();
            spriteGeometry.hostToDevice();
        }
    }

    static public Geometry getGeometry()
    {
        if( spriteGeometry == null )
        {
            reload();
        }
        return spriteGeometry;
    }
}

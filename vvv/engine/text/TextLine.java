/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.text;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import org.lwjgl.util.vector.Vector3f;
import vvv.engine.Geometry;
import vvv.engine.Geometry.VertexAttribs;
import vvv.engine.Geometry.VertexAttribs.VERTEX_ATTRIBUTE;
import vvv.engine.layers.PositionProperties;

/**
 *
 * @author Вячеслав
 */
public class TextLine
{
    private Geometry geometry = new Geometry();
    private Font     font = null;
    private String   text = "";
    private float    width = 0;
    private Vector3f color = new Vector3f(1,1,1);
    private PositionProperties position = new PositionProperties();
    private boolean  changed = true;
    
    static private final VertexAttribs attribs = new Geometry.VertexAttribs();
    {
        attribs.add( VERTEX_ATTRIBUTE.POSITION, 3, GL_FLOAT );
        attribs.add( VERTEX_ATTRIBUTE.TEXCOORD, 2, GL_FLOAT );
    }
    
    
    private ByteBuffer vbb = null;
    private ByteBuffer ibb = null;
    private int        numIndices = 0;
    private int        numVertices = 0;
    
    private static final int VERTICES_PER_SYMBOL = 4;
    private static final int INDICES_PER_SYMBOL = 6;
    
    public void setText( String text) 
    {
        this.text = text;
        changed = true;
    }
    
    public void setFont( Font font )
    {
        this.font = font;
        changed = true;
    }
    
    public void setPosition( float x, float y)
    {
        position.setPosition(x, y, 0);
    }
    
    public void setPosition( float x, float y, float z)
    {
        position.setPosition(x, y, z);
    }
    
    public String getText()
    {
        return text;
    }
    
    private void genMesh()
    {
        int len = text.length();
        this.numVertices = len*VERTICES_PER_SYMBOL;
        this.numIndices  = len*INDICES_PER_SYMBOL;
        
        final int numVerticesBytes = numVertices * attribs.getStride();
        final int numIndicesBytes = numIndices * 4;
        
        if( vbb == null || vbb.capacity() < numVerticesBytes)
        {
            vbb = BufferUtils.createByteBuffer( numVerticesBytes );    
        }
        
        if( ibb == null || ibb.capacity() < numIndicesBytes)
        {
            ibb = BufferUtils.createByteBuffer(numIndicesBytes);
        }
        
        for( int i=0; i < len; ++i )
        {
            char symbol = text.charAt(i);
            Font.GlyphInfo gi ;
            try
            {
                gi = font.getGlyphInfo(symbol);
            }
            catch(Exception e1)
            {
                try                
                {
                    gi = font.getGlyphInfo('?');
                }
                catch (Exception ex)
                {
                    try
                    {
                        gi = font.getGlyphInfo(' ');
                    }
                    catch (Exception ex1)
                    {
                    }      
                }  
                Logger.getLogger(TextLine.class.getName()).log(Level.SEVERE, null, e1);
            }
        }
        
    }
    
    private void updateGeometry()
    {
        if( changed )
        {
            genMesh();
        }
    }
    
    public void draw()
    {
        
    } 
}

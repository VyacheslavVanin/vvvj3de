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
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import org.lwjgl.util.vector.Vector3f;
import vvv.engine.Geometry;
import vvv.engine.Geometry.VertexAttribs;
import vvv.engine.Geometry.VertexAttribs.VERTEX_ATTRIBUTE;
import vvv.engine.texture.Texture;

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

    private boolean  changed = true;
    
    static private final VertexAttribs attribs = new Geometry.VertexAttribs();
    static {
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
      
    public String getText()
    {
        return text;
    }
    
    private Font.GlyphInfo getGlyphInfo( char symbol)
    {
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
                    throw new IllegalArgumentException("symbol not found");
                }      
            }  
            Logger.getLogger(TextLine.class.getName()).log(Level.SEVERE, null, e1);
        }
        return gi;
    }
    
    private void putVertex( ByteBuffer b, float x, float y, float z, 
                                          float tx, float ty)
    {
        b.putFloat( x );
        b.putFloat( y);
        b.putFloat( z);
        b.putFloat( tx );
        b.putFloat( ty );
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
        
        ibb.clear();
        vbb.clear();
        
        int x = 0;
        int y = 0;
        int index = 0;
        for( int i=0; i < len; ++i )
        {
            char symbol = text.charAt(i);
            Font.GlyphInfo gi = getGlyphInfo(symbol);
            float left      = x + gi.offsetX;
            float right     = left + gi.glyphWidth;
            float top       = y + gi.offsetY;
            float bottom    = top - gi.glyphHeight;
            float texLeft   = gi.tx;
            float texRight  = texLeft + gi.twidth;
            float texTop    = gi.ty;
            float texBottom = texTop - gi.theight;
            putVertex( vbb, left, bottom, 0, texLeft, texBottom);
            putVertex( vbb, left, top,    0, texLeft, texTop   );
            putVertex( vbb, right, top,   0, texRight, texTop  );
            putVertex( vbb, right, bottom,0, texRight, texBottom);
            x += gi.width;
            
            ibb.putInt(index + 0);
            ibb.putInt(index + 1);
            ibb.putInt(index + 3);
            ibb.putInt(index + 1);
            ibb.putInt(index + 2);
            ibb.putInt(index + 3);
            index += 4;
        }
        vbb.flip();
        ibb.flip();
        
        geometry.loadToHost(vbb, attribs, ibb, numIndices, GL_UNSIGNED_INT);    
    
        this.width = x;
    }
    
    public final float getLineWidth()
    {
        updateGeometry();
        return this.width;
    }
    
    public final float getAscenderHight()
    {
        return font.getAscenderHight();
    }
    
    public final float getDescenderHight()
    {
        return font.getDescenderHight();
    }
    
    private void updateGeometry()
    {
        if( changed )
        {
            genMesh();
        }
    }
    
  
    public Texture getTexture()
    {
        return font.getTexture();
    }
    
 
    public void draw()
    {
        updateGeometry();
        geometry.activate();
        geometry.draw();
    }     
}

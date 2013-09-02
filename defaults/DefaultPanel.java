/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import vvv.engine.Geometry;
import vvv.engine.texture.Texture;
import vvv.engine.widgets.Panel;
import vvv.engine.widgets.Widget;

/**
 *
 * @author Вячеслав
 */
public class DefaultPanel extends Panel
{
    private Geometry geometry = null;
    private Texture  texture  = null;
    
    public DefaultPanel()
    {
        try
        {
            texture  = gui.getPanelTexture();
        }
        catch (IOException ex)
        {
            Logger.getLogger(DefaultPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        geometry = new Geometry();
    }
 
    
    @Override
    protected void onAddWidget(Widget wgt)
    {
        
    }

    @Override
    protected void onRemoveWidget(Widget wgt)
    {
        
    }

    @Override
    protected void onDraw() throws Exception
    {
        
        
        float h = 0;
        List<Widget> list = getChildren();
        for (Widget wgt : list)
        {
            h += wgt.getHeight() + 2;
            wgt.setPosition(0, h);
        }
    }

    @Override
    protected void onSetSize(float w, float h)
    {
        super.onSetSize(w, h); 
        updateGeometry(  w, h);
    }

    static private final Geometry.VertexAttribs attribs = new Geometry.VertexAttribs();
    static {
        attribs.add( Geometry.VertexAttribs.VERTEX_ATTRIBUTE.POSITION, 3, GL_FLOAT );
        attribs.add( Geometry.VertexAttribs.VERTEX_ATTRIBUTE.TEXCOORD, 2, GL_FLOAT );
    }
    
    private ByteBuffer vbb = 
            BufferUtils.createByteBuffer( 16 * attribs.getStride() );
    private ByteBuffer ibb = 
            BufferUtils.createByteBuffer( 9*2*3*4); 
            /* 9 squares, 2 triangles per square,
             * 3 indeces per triangle, 4 bytes per index */
    
    private void fillIndexBuffer()
    {
        ibb.clear();
        for( int j = 0; j < 3; ++j)
        {
            for( int i = 0; i < 3; ++i)
            {
                int j4 = j*4;
                ibb.putInt(i + j4 + 0);
                ibb.putInt(i + j4 + 1);
                ibb.putInt(i + j4 + 5);
                ibb.putInt(i + j4 + 0);
                ibb.putInt(i + j4 + 5);
                ibb.putInt(i + j4 + 4);
            }
        }
    }
    
    private void putVertex(  float x, float y, float z, 
                             float u, float v )
    {
        vbb.putFloat(x);
        vbb.putFloat(y);
        vbb.putFloat(z);
        vbb.putFloat(u);
        vbb.putFloat(v);
    }
    
    private void fillVertexBuffer( float width, float height,
                                   float topBorder, float bottomBorder,
                                   float leftBorder, float rightBorder)
    {
        vbb.clear();
        float high = height;
        float low = 0;
        float highMidle = high - topBorder;
        float lowMidle  = low + bottomBorder;
        
        float left = 0; 
        float right = width;
        float leftMidle = left + leftBorder;
        float rightMidle  = right - rightBorder;
        
        putVertex( left,      high,    0,    0f, 1f);
        putVertex( leftMidle, high,    0, 0.25f, 1f);
        putVertex( rightMidle,high,    0, 0.75f, 1f);
        putVertex( right    , high,    0,    1f, 1f);
        
        putVertex( left,      highMidle,    0,    0f, 0.75f);
        putVertex( leftMidle, highMidle,    0, 0.25f, 0.75f);
        putVertex( rightMidle,highMidle,    0, 0.75f, 0.75f);
        putVertex( right    , highMidle,    0,    1f, 0.75f);
        
        putVertex( left,      lowMidle,    0,    0f, 0.25f);
        putVertex( leftMidle, lowMidle,    0, 0.25f, 0.25f);
        putVertex( rightMidle,lowMidle,    0, 0.75f, 0.25f);
        putVertex( right    , lowMidle,    0,    1f, 0.25f);
        
        putVertex( left,      low,    0,    0f, 0f );
        putVertex( leftMidle, low,    0, 0.25f, 0f );
        putVertex( rightMidle,low,    0, 0.75f, 0f );
        putVertex( right    , low,    0,    1f, 0f );
    }
    
    private void updateGeometry(float w, float h)
    {
        fillVertexBuffer( h, h, 4, 4, 4, 4 );
    }
    
}

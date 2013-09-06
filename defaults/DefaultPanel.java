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
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import vvv.engine.Camera;
import vvv.engine.Geometry;
import vvv.engine.shader.ModelShader;
import vvv.engine.texture.Texture;
import vvv.engine.widgets.Layout;
import vvv.engine.widgets.Panel;
import vvv.engine.widgets.PositionProperties;
import vvv.engine.widgets.VerticalLayout;
import vvv.engine.widgets.Widget;

/**
 *
 * @author Вячеслав
 */
public class DefaultPanel extends Panel
{
    private Geometry geometry = null;
    private Texture  texture  = null;
    private static final float DEFAULT_SIZE = 100.0f;
    private static final float DEFAULT_BORDER = 4.0f;
    private Vector4f color = new Vector4f(1,1,1,1);
    private PositionProperties position = new PositionProperties();
    
    private Layout  layout = null;
    
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
        
        layout   = new VerticalLayout();
        
        addChild(layout);
        geometry = new Geometry();
        GridMeshCreator3x3.fillIndexBuffer(ibb);
        setSize(DEFAULT_SIZE, DEFAULT_SIZE);
    }
 
    
    @Override
    protected void onAddWidget(Widget wgt)
    {
        rearrange();
    }

    @Override
    protected void onRemoveWidget(Widget wgt)
    {
        rearrange();
    }

    @Override
    public boolean addWidget(Widget wgt)
    {
        return layout.addWidget(wgt); 
    }

    @Override
    public boolean removeWidget(Widget wgt)
    {
        return layout.removeWidget(wgt); 
    }

    
    
    
    public void setColor(float r, float g, float b, float a)
    {
        color.set(r, g, b, a);
    }
    
    static private Matrix4f tmp = new Matrix4f();
    private void drawPanel() throws Exception
    {
        ModelShader sh = gui.getPanelShader();
        Camera cam     = getCamera();
        
        Defaults.enableTransparency();
            sh.activate();
            sh.setColor(0, color);
                Matrix4f.mul( cam.getViewProjectionMatrix4f(), 
                              position.getMatrix4f(), 
                              tmp);
            sh.setModelViewProjectionMatrix(tmp);
            sh.setTexture(0, texture);
            geometry.activate();
            geometry.draw();
        Defaults.disableTransparency();
    }
    
    @Override
    protected void onDraw() throws Exception
    {     
        drawPanel();   
    }

    @Override
    protected void onSetPosition(float x, float y)
    {
        super.onSetPosition( x, y );
        position.setPosition( getGlobalPosX(),
                              getGlobalPosY(), 
                              0);
        rearrange();
    }

    private void rearrange()
    {
        List<Widget> list = getChildren();
        for (Widget wgt : list)
        {
            wgt.setPosition( wgt.getPosX(), wgt.getPosY());
        }   
    }
    
    @Override
    protected void onSetSize(float w, float h)
    {
        super.onSetSize( w, h); 
        updateGeometry(  w, h);
        layout.setSize(w, h);
        rearrange();
    }

    static private final Geometry.VertexAttribs attribs = new Geometry.VertexAttribs();
    static {
        attribs.add( Geometry.VertexAttribs.VERTEX_ATTRIBUTE.POSITION, 3, GL_FLOAT );
        attribs.add( Geometry.VertexAttribs.VERTEX_ATTRIBUTE.TEXCOORD, 2, GL_FLOAT );
    }
    
    private ByteBuffer vbb = 
            BufferUtils.createByteBuffer( 16 * attribs.getStride() );
    static private final int INDEX_BUFFER_SIZE = 9*2*3*4;
    private ByteBuffer ibb = 
            BufferUtils.createByteBuffer( INDEX_BUFFER_SIZE); 
            /* 9 squares, 2 triangles per square,
             * 3 indeces per triangle, 4 bytes per index */
    
   
    private void updateGeometry(float w, float h)
    {
        GridMeshCreator3x3.fillVertexBuffer( vbb, w, h, 4, 4, 4, 4 );
        geometry.loadToHost(vbb, attribs, ibb, 
                            INDEX_BUFFER_SIZE, GL_UNSIGNED_INT);
    }
    
}

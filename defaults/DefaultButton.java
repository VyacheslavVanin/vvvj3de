/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.io.IOException;
import java.nio.ByteBuffer;
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
import vvv.engine.text.Font;
import vvv.engine.texture.Texture;
import vvv.engine.widgets.AbstractButton;
import vvv.engine.widgets.PositionProperties;
import vvv.engine.widgets.TextLabel;
/**
 *
 * @author QwertyVVV
 */
public class DefaultButton extends AbstractButton
{
    private TextLabel text = new TextLabel("Button");
    private Texture   checked = null;
    private Texture   unchecked = null;
    private Texture   texture = null;
    
    private Geometry geometry = null;
    private static final float BORDER_WIDTH = 4;
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
    
    private Vector4f color = new Vector4f(1,1,1,1);
    private PositionProperties position = new PositionProperties();
    
    public  DefaultButton()
    {
        try
        {
            checked = Gui.getButtonCheckedTexture();
            unchecked = Gui.getButtonUncheckedTexture();
        }
        catch( IOException ex)
        {
            Logger.getLogger(DefaultPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        texture = unchecked;
        
        geometry = new Geometry();
        GridMeshCreator3x3.fillIndexBuffer(ibb);
        
        
        addChild( text );
        text.setColor(0, 0, 0, 1);
        setSize( text.getWidth(), text.getHeight() );
        setPosition(0, 0);
        updateTextPosition();
    }
    
    private void updateGeometry( float w, float h)
    {
        GridMeshCreator3x3.fillVertexBuffer(vbb, w, h, 
                            BORDER_WIDTH, BORDER_WIDTH, 
                            BORDER_WIDTH, BORDER_WIDTH);
        geometry.loadToHost(vbb, attribs, ibb, 
                            INDEX_BUFFER_SIZE, GL_UNSIGNED_INT);
        
        
    }
    
    private void updateTextPosition()
    {
        float textX = (int)(getWidth() - text.getWidth())/2;
        float textY = (int)(getHeight()- text.getHeight())/2;
        
        text.setPosition(textX, textY);
    }
    
    
    public void setColor( float r, float g, float b, float a )
    {
        color.set(r, g, b, a);
    }
    
    public void setTextColor(float r, float g, float b, float a)
    {
        text.setColor(r, g, b, a);
    }
    
    public void setText( String text)
    {
        this.text.setText(text);
        updateTextPosition();
    }
    
    public void setFont( Font font )
    {
        this.text.setFont(font);
        updateTextPosition();
    }
    
    
    
    @Override
    protected void onClick()
    {
    }

    @Override
    protected void onMouseEnter()
    {
        color.x *= 1.5f;
        color.y *= 1.5f;
        color.z *= 1.5f;
    }

    @Override
    protected void onMouseLeave()
    {
        color.x /= 1.5f;
        color.y /= 1.5f;
        color.z /= 1.5f;
    }

    @Override
    protected void onPress()
    {
        texture = checked;
    }

    @Override
    protected void onRelease()
    {
        texture = unchecked;
    }
    
    static private  Matrix4f tmp = new Matrix4f();
    private void drawButton() throws Exception
    {
        ModelShader sh = Gui.getColorMapShader();
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
        drawButton();
    }

    @Override
    protected void onSetPosition(float x, float y)
    {
        position.setPosition( getGlobalPosX(),
                              getGlobalPosY(), 
                              0);
        updateTextPosition();
    }

    @Override
    protected void onSetSize(float w, float h)
    {
        updateGeometry(  w, h);
        updateTextPosition();
    }
    
}

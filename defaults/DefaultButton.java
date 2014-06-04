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
import vvv.engine.widgets.TextLabel;

/**
 *
 * @author QwertyVVV
 */
public class DefaultButton extends AbstractButton {

    private final TextLabel text = new TextLabel("Button");
    private Texture checked = null;
    private Texture unchecked = null;
    private Texture currentTexture = null;

    private Geometry geometry = null;
    private static final float BORDER_WIDTH = 4;
    private boolean  autosize = true;
    
    
    static private final Geometry.VertexAttribs attribs = new Geometry.VertexAttribs();

    static 
    {
        attribs.add(Geometry.VertexAttribs.VERTEX_ATTRIBUTE.POSITION, 3, GL_FLOAT);
        attribs.add(Geometry.VertexAttribs.VERTEX_ATTRIBUTE.TEXCOORD, 2, GL_FLOAT);
    }
    private final ByteBuffer vbb = BufferUtils.createByteBuffer(16 * attribs.getStride());
    static private final int NUM_INDICES = 9 * 2 * 3;
    static private final int INDEX_BUFFER_SIZE = NUM_INDICES * 4;
    private final ByteBuffer ibb  = BufferUtils.createByteBuffer(INDEX_BUFFER_SIZE);
    /* 9 squares, 2 triangles per square,
     * 3 indeces per triangle, 4 bytes per index */

    private final Vector4f color = new Vector4f(1, 1, 1, 1);

    public DefaultButton() {
        try {
            checked = DefaultGui.getButtonCheckedTexture();
            unchecked = DefaultGui.getButtonUncheckedTexture();
        } catch (IOException ex) {
            Logger.getLogger(DefaultPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentTexture = unchecked;

        geometry = new Geometry();
        GridMeshCreator3x3.fillIndexBuffer(ibb);

        addChild(text);
        text.setColor(0, 0, 0, 1);
        setSize(text.getWidth(), text.getHeight());
        setPosition(0, 0);
        updateTextPosition();
    }
    
    public DefaultButton( String label)
    {
        this();
        setText(label); 
    }

    private void updateGeometry(float w, float h) {
        GridMeshCreator3x3.fillVertexBuffer(vbb, w, h,
                BORDER_WIDTH, BORDER_WIDTH,
                BORDER_WIDTH, BORDER_WIDTH);
        geometry.loadToHost(vbb, attribs, ibb,
                NUM_INDICES, GL_UNSIGNED_INT);

    }

    private void updateTextPosition() {
        float textX = (int) (getWidth() - text.getWidth()) / 2;
        float textY = (int) (getHeight() - text.getHeight()) / 2;

        text.setPosition(textX, textY);
    }

    public void setColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
    }

    public void setTextColor(float r, float g, float b, float a) {
        text.setColor(r, g, b, a);
    }

    public void setText(String text) {
        this.text.setText(text);
        
        if(autosize)
        {
            final float w = this.text.getWidth() + 2*BORDER_WIDTH;
            final float h = this.text.getHeight() + 2*BORDER_WIDTH;
            setSize( w, h );
        }
        
        updateTextPosition();
    }

    public void setFont(Font font) {
        this.text.setFont(font);
        updateTextPosition();
    }

    @Override
    protected void onMouseEnter() {
        color.x *= 1.5f;
        color.y *= 1.5f;
        color.z *= 1.5f;
    }

    @Override
    protected void onMouseLeave() {
        color.x /= 1.5f;
        color.y /= 1.5f;
        color.z /= 1.5f;
    }

    @Override 
    protected void onDisable()
    {
        color.x /= 1.5f;
        color.y /= 1.5f;
        color.z /= 1.5f;
    }
    
     @Override 
    protected void onEnable()
    {
        color.x *= 1.5f;
        color.y *= 1.5f;
        color.z *= 1.5f;
    }
    
    @Override
    protected void onPress() {
        currentTexture = checked;
    }

    @Override
    protected void onRelease() {
        currentTexture = unchecked;
    }

    static private Matrix4f tmp = new Matrix4f();

    private void drawButton() throws Exception {
        ModelShader sh = DefaultGui.getColorMapShader();
        Camera cam = getCamera();

        Defaults.enableTransparency();
        sh.activate();
        sh.setColor(0, color);
        Matrix4f.mul(cam.getViewProjectionMatrix4f(),
                position.getMatrix4f(),
                tmp);
        sh.setModelViewProjectionMatrix(tmp);
        sh.setTexture(0, currentTexture);
        geometry.activate();
        geometry.draw();
        Defaults.disableTransparency();
    }

    @Override
    protected void onDraw() throws Exception {
        drawButton();
    }

    @Override
    protected void onSetPosition(float x, float y) {
        super.onSetPosition(x, y);
        updateTextPosition();
    }

    @Override
    protected void onSetSize(float w, float h) {
        updateGeometry(w, h);
        updateTextPosition();
    }

    public void setAutoSize(boolean b)
    {
        autosize = b;
    }
    
}

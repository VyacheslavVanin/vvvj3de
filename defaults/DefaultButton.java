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
import vvv.engine.Camera;
import vvv.engine.Color;
import vvv.engine.ConstColor;
import vvv.engine.Geometry;
import vvv.engine.VariableColor;
import vvv.engine.shader.ModelShader;
import vvv.engine.text.Font;
import vvv.engine.texture.Texture;
import vvv.engine.widgets.AbstractButton;
import vvv.engine.widgets.TextLabel;

/**
 *
 * @author QwertyVVV
 */
public final class DefaultButton extends AbstractButton {

    private final TextLabel text = new TextLabel("Button");
    private Texture checked = null;
    private Texture unchecked = null;
    private Texture currentTexture = null;

    private Geometry geometry = null;
    private static final int BORDER_WIDTH = 4;
    private boolean autosize = true;

    static private final Geometry.VertexAttribs attribs = new Geometry.VertexAttribs();

    static {
        attribs.add(Geometry.VertexAttribs.VERTEX_ATTRIBUTE.POSITION, 3, GL_FLOAT);
        attribs.add(Geometry.VertexAttribs.VERTEX_ATTRIBUTE.TEXCOORD, 2, GL_FLOAT);
    }
    private final ByteBuffer vbb = BufferUtils.createByteBuffer(16 * attribs.getStride());
    static private final int NUM_INDICES = 9 * 2 * 3;
    static private final int INDEX_BUFFER_SIZE = NUM_INDICES * 4;
    private final ByteBuffer ibb = BufferUtils.createByteBuffer(INDEX_BUFFER_SIZE);
    /* 9 squares, 2 triangles per square,
     * 3 indeces per triangle, 4 bytes per index */

    private Color color = DefaultSystemColors.getControlColor();
    private float colorIntensity = 1;
    
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
        text.setColor( ConstColor.BLACK );   
        setPosition(0, 0);
        updateTextPosition();
    }

    public DefaultButton(String label) {
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
        final int widgetWidth = getWidth();
        final int widgetHeight= getHeight();
            
        final int textX = (int) (widgetWidth - text.getWidth()) / 2;
        final int textY = (int) (widgetHeight - text.getHeight()) / 2;
        
        if(autosize == false)
        {
            text.setSize( widgetWidth - 2*BORDER_WIDTH , widgetHeight  - 2*BORDER_WIDTH);
        }
        
        text.setPosition(textX, textY);
    }


    public final void setColor(Color c) {
        color = c;
    }

    
    public final void setTextColor( Color c)
    {
        text.setColor(c);
    }

    public final void setText(String text) {
        this.text.setText(text);

        if (autosize) {
            final int w = this.text.getWidth() + 2 * BORDER_WIDTH;
            final int h = this.text.getHeight() + 2 * BORDER_WIDTH;
            privateSetSize(w, h);
        }

        updateTextPosition();
    }

    public final void setFont(Font font) {
        this.text.setFont(font);
        updateTextPosition();
    }

    @Override
    protected void onMouseEnter() {
        colorIntensity = 1.5f;
    }

    @Override
    protected void onMouseLeave() {
        colorIntensity = 1;
    }

    @Override
    protected void onDisable() {
        colorIntensity = 1/1.5f;
    }

    @Override
    protected void onEnable() {
        colorIntensity = 1;
    }

    @Override
    protected void onPress() {
        currentTexture = checked;
    }

    @Override
    protected void onRelease() {
        currentTexture = unchecked;
    }

    private static final Matrix4f tmp = new Matrix4f();
    private static final VariableColor colorToDraw = new VariableColor();
    private void drawButton() throws Exception {
        ModelShader sh = DefaultGui.getColorMapShader();
        Camera cam = getCamera();

        colorToDraw.setColor(color);
        colorToDraw.mulColor(colorIntensity);
        Defaults.enableTransparency();
        sh.activate();
        sh.setColor(0, colorToDraw);
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
    protected void onSetSize(int w, int h) {
        setAutoSize(false);
    }

    private void privateSetSize(int w, int h)
    {
        setWidth(w);
        setHeight(h);
        updateGeometry(w, h);
        updateTextPosition(); 
    }
    
    public final void setAutoSize(boolean b) {
        autosize = b;
        text.setAutoSize(b);
    }

    @Override
    protected void onRefresh() {
        final int w = getWidth();
        final int h = getHeight();
        updateGeometry(w, h);
        updateTextPosition(); 
    }

}

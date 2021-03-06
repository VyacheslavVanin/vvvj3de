/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.nio.ByteBuffer;
import java.util.List;
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
import vvv.engine.shader.ModelShader;
import vvv.engine.texture.Texture;
import vvv.engine.widgets.Layout;
import vvv.engine.widgets.Panel;
import vvv.engine.widgets.VerticalLayout;
import vvv.engine.widgets.Widget;

/**
 *
 * @author Вячеслав
 */
public class DefaultPanel extends Panel {

    private Geometry geometry = null;
    private Texture texture = null;
    private static final int DEFAULT_SIZE = 100;
    private static final int DEFAULT_BORDER = 4;

    static private final Geometry.VertexAttribs attribs = new Geometry.VertexAttribs();

    static {
        attribs.add(Geometry.VertexAttribs.VERTEX_ATTRIBUTE.POSITION, 3, GL_FLOAT);
        attribs.add(Geometry.VertexAttribs.VERTEX_ATTRIBUTE.TEXCOORD, 2, GL_FLOAT);
    }

    private final ByteBuffer vbb = BufferUtils.createByteBuffer(16 * attribs.getStride());
    /* 9 squares, 2 triangles per square,
     * 3 indeces per triangle, 4 bytes per index */
    static private final int NUM_INDICES = 9 * 2 * 3;
    static private final int INDEX_BUFFER_SIZE = NUM_INDICES * 4;
    private final ByteBuffer ibb = BufferUtils.createByteBuffer(INDEX_BUFFER_SIZE);

    private Color color = DefaultSystemColors.getPanelColor();

    private Layout layout = null;

    public DefaultPanel() {
        try {
            texture = DefaultGui.getPanelTexture();
        } catch (Exception ex) {
            Logger.getLogger(DefaultPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        layout = new VerticalLayout();

        addChild(layout);
        geometry = new Geometry();
        GridMeshCreator3x3.fillIndexBuffer(ibb);
        setSize(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public DefaultPanel(int width, int height) {
        this();
        setSize(width, height);
    }

    @Override
    protected void onAddWidget(Widget wgt) {
        rearrange();
    }

    @Override
    protected void onRemoveWidget(Widget wgt) {
        rearrange();
    }

    @Override
    public boolean addWidget(Widget wgt) {
        return layout.addWidget(wgt);
    }

    @Override
    public boolean removeWidget(Widget wgt) {
        return layout.removeWidget(wgt);
    }

    public void setColor(float r, float g, float b, float a) {
        color = new ConstColor(r, g, b, a);
    }

    public void setColor(Color c) {
        color = c;
    }

    private static final Matrix4f tmp = new Matrix4f();

    private void drawPanel() throws Exception {
        ModelShader sh = DefaultGui.getPanelShader();
        Camera cam = getCamera();

        Defaults.enableTransparency();
        sh.activate();
        sh.setColor(0, color);
        Matrix4f.mul(   cam.getViewProjectionMatrix4f(),
                        position.getMatrix4f(),
                        tmp);
        sh.setModelViewProjectionMatrix(tmp);
        sh.setTexture(0, texture);
        geometry.activate();
        geometry.draw();
        Defaults.disableTransparency();
    }

    @Override
    protected void onDraw() throws Exception {
        drawPanel();
    }

    private void rearrange() {
        List<Widget> list = getChildren();
        final int size = list.size();
        for ( int i = 0; i < size; ++i) 
        {
            final Widget wgt = list.get(i);
            wgt.setPosition(wgt.getPosX(), wgt.getPosY());
        }
    }


    private void updateGeometry(float w, float h) {
        GridMeshCreator3x3.fillVertexBuffer(vbb, w, h, 4, 4, 4, 4);
        geometry.loadToHost(vbb, attribs, ibb,
                NUM_INDICES, GL_UNSIGNED_INT);
    }

    @Override
    protected void onRefresh() {
        final int w = getWidth();
        final int h = getHeight();
        updateGeometry(w, h);
        layout.setSize(w, h);
        rearrange();
    }

}

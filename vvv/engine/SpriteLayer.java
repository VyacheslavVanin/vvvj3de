/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import org.lwjgl.util.vector.Matrix4f;
import vvv.math.Vec3;

/**
 *
 * @author QwertyVVV
 */
public class SpriteLayer extends Layer
{

    public SpriteLayer()
    {
        super();
        init();
    }

    @Override
    public void draw() throws Exception
    {
        if (shader == null)
        {
            throw new Exception("no assigned Shader to layer");
        }
        List<GraphicObject> objects = getObjects();

        spriteGeometry.activate();
        shader.activate();

        for (GraphicObject go : objects)
        {
            // all objects in list are instances of Sprite
            // (was checked onAddObject). So it's safe to cast.
            Sprite spr = (Sprite) go;
            shader.setTexture(0, spr.getTexture());

            if (camera.isChanged())
            {
                float16ToMatrix4f(camera.getViewProjection(), vpmatrix);
            }

            Matrix4f.mul(vpmatrix, spr.getMatrix4f(), tmp);

            shader.setMoodelViewProjectionMatrix(tmp);
            spriteGeometry.draw();
        }
    }

    private void float16ToMatrix4f(float[] f, Matrix4f m)
    {
        floatBuffer16.position(0);
        floatBuffer16.put(f);
        floatBuffer16.position(0);
        m.load(floatBuffer16);
        floatBuffer16.position(0);
        m.transpose(m);
    }

    @Override
    public void onResize()
    {
        float h = getHeight();
        float w = getWidth();
        camera.setOrtho(h / 2, -h / 2, -w / 2, w / 2, -1, 1);
        camera.setBodyForward(new Vec3(0, 0, 1), new Vec3(0, 1, 0));
        camera.setPos(0, 0, 0);
        camera.lookAt(0, 0, 1);
    }

    @Override
    protected boolean onAddObject(GraphicObject obj)
    {
        if (obj instanceof Sprite)
        {
            return true;
        }
        return false;
    }

    @Override
    protected boolean onRemoveObject(GraphicObject obj)
    {
        return true;
    }

    @Override
    public void init()
    {
        initGeometry();
        initCamera();
        floatBuffer16 = BufferUtils.createFloatBuffer(16);
    }

    private void initCamera()
    {
        camera = new Camera();
        camera.setOrtho(1, -1, -1, 1, -1, 1);
        camera.setBodyForward(new Vec3(0, 0, 1), new Vec3(0, 1, 0));
        camera.lookAt(0, 0, 1);
    }

    private void initGeometry()
    {
        spriteGeometry = new Geometry();
        float[] vertices =
        {
            -0.5f, -0.5f, 1, 0, 0,
            -0.5f, 0.5f, 1, 0, 1,
            0.5f, 0.5f, 1, 1, 1,
            0.5f, -0.5f, 1, 1, 0
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

        Geometry.VertexAttribute[] attribs =
        {
            new Geometry.VertexAttribute(Constants.ATTRIBUTE_VERTEX_POSITION,
                                         3, GL_FLOAT),
            new Geometry.VertexAttribute(Constants.ATTRIBUTE_VERTEX_TEXCOORD,
                                         2, GL_FLOAT)
        };
        spriteGeometry.loadToHost(vbb, attribs, ibb, indices.length, GL_UNSIGNED_INT);
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setShader(ShaderModel shader)
    {
        this.shader = shader;
    }
    private Geometry spriteGeometry;
    private Camera camera;
    private ShaderModel shader = null;
    private FloatBuffer floatBuffer16;
    private Matrix4f tmp = new Matrix4f();
    private Matrix4f vpmatrix = new Matrix4f();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.layers;

import java.io.IOException;
import vvv.engine.shader.ModelShader;
import vvv.engine.sprite.Sprite;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import vvv.engine.Camera;
import vvv.engine.Geometry;
import vvv.engine.Geometry.VertexAttribs;
import vvv.engine.Geometry.VertexAttribs.VERTEX_ATTRIBUTE;
import vvv.engine.Globals;
import vvv.engine.text.Font;
import vvv.engine.text.TextLine;
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
        try
        {
            f = Font.loadFromFiles("fonts/arial20.png");
        }
        catch (IOException ex)
        {
            Logger.getLogger(SpriteLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        textShader = new ModelShader();
        try
        {
            textShader.loadFromFiles("shaders/text.vs", "shaders/text.fs");
        }
        catch (IOException ex)
        {
            Logger.getLogger(SpriteLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        text = new TextLine();
        text.setFont(f);
        text.setText("Text text 1");
        text.setPosition(0, 0, 0.5f);
        
    }

    // it is work but incorrect 
    // (it have to check only camera data to determinate, not getHeight, Width)
    private boolean isInView(Sprite spr)
    {
        float x = spr.getScale().x * 0.5f;
        float y = spr.getScale().y * 0.5f;

        if( spr.getPosition().y - y > camera.getPos().y() + getHeight()*0.5f )
            return false;
        if( spr.getPosition().y + y < camera.getPos().y() - getHeight()*0.5f )
            return false;
        if( spr.getPosition().x - x > camera.getPos().x() + getWidth()*0.5f )
            return false;
        if( spr.getPosition().x + x < camera.getPos().x() - getWidth()*0.5f )
            return false;
        
        return true;
    }
    
    @Override
    public void draw() throws Exception
    {
        if (shader == null)
        {
            throw new Exception("no assigned Shader to layer");
        }
        List<GraphicObject> objects = getObjects();
        
        if (camera.isChanged())
        {
            float16ToMatrix4f(camera.getViewProjection(), vpmatrix);
        }

       
        
        
        spriteGeometry.activate();
        shader.activate();
     
        Globals.Time.update();
        
        for( int i = 0; i < objects.size(); ++i)
        {
            GraphicObject go = objects.get(i);
            // all objects in list are instances of Sprite
            // (was checked onAddObject). So it's safe to cast.
            Sprite spr = (Sprite) go;     
            
            if( isInView(spr))
            { 
                shader.setTexture(0, spr.getTexture() );
                Matrix4f.mul(vpmatrix, spr.getMatrix4f(), tmp);
                shader.setMoodelViewProjectionMatrix(tmp);
                spriteGeometry.draw();
            }
        } 
        
        text.setText( "" + Calendar.getInstance().toString() );
        
        textShader.activate();
        text.activateGeometry();
        textShader.setColor(0, new Vector4f(1, 1, 1, 1));
        Matrix4f.mul(vpmatrix, text.getMatrix4f(), tmp);
        textShader.setMoodelViewProjectionMatrix(tmp);
        textShader.setTexture( 0, text.getTexture());
        text.draw();
        
    }

    private Random r = new Random(System.currentTimeMillis());
    
    
    private void float16ToMatrix4f(float[] f, Matrix4f m)
    {
        floatBuffer16.position(0);
        floatBuffer16.put(f);
        floatBuffer16.position(0);
        m.load(floatBuffer16);
        floatBuffer16.position(0);
    }

    @Override
    public void onResize()
    {
        float h = getHeight();
        float w = getWidth();
        camera.setOrtho(h / 2, -h / 2, -w / 2, w / 2, -2, 2);
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

        
        VertexAttribs attribs = new Geometry.VertexAttribs();
        attribs.add( VERTEX_ATTRIBUTE.POSITION, 3, GL_FLOAT );
        attribs.add( VERTEX_ATTRIBUTE.TEXCOORD, 2, GL_FLOAT );
        
        spriteGeometry.loadToHost(vbb, attribs, ibb, indices.length, GL_UNSIGNED_INT);
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setShader(ModelShader shader)
    {
        this.shader = shader;
    }
    
    private Font f;
    private TextLine text;
    private ModelShader textShader;
    
    private Geometry spriteGeometry;
    private Camera camera;
    private ModelShader shader = null;
    private FloatBuffer floatBuffer16;
    private Matrix4f tmp = new Matrix4f();
    private Matrix4f vpmatrix = new Matrix4f();
}

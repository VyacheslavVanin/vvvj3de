/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import vvv.engine.shader.ModelShader;
import vvv.engine.sprite.Sprite;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import vvv.engine.Camera;
import vvv.engine.Geometry;
import vvv.engine.Globals;
import vvv.engine.sprite.SpriteGeometry;
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
        
        vpmatrix = camera.getViewProjectionMatrix4f();
       
        Geometry sg = SpriteGeometry.getGeometry();
        sg.activate();
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
                shader.setModelViewProjectionMatrix(tmp);
                sg.draw();
            }
        }      
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

    public final void init()
    {
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

    public Camera getCamera()
    {
        return camera;
    }

    public void setShader(ModelShader shader)
    {
        this.shader = shader;
    }
      
    private Camera camera;
    private ModelShader shader = null;
    private FloatBuffer floatBuffer16;
    private Matrix4f tmp = new Matrix4f();
    private Matrix4f vpmatrix = new Matrix4f();
}

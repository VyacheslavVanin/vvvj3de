/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import vvv.engine.Camera;
import vvv.engine.Geometry;
import vvv.engine.Globals;
import vvv.engine.shader.ModelShader;
import vvv.engine.sprite.Sprite;
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

    private Vector4f res = new Vector4f();
    private Vector4f src = new Vector4f();
    
    
    private boolean isInView(Sprite spr)
    {
        float x = spr.getScale().x * 0.5f;
        float y = spr.getScale().y * 0.5f;

        Vector3f v3 = spr.getPosition();
        src.set(v3.x+x, v3.y+y, v3.z, 1);
        Matrix4f.transform(camera.getViewProjectionMatrix4f(), src, res);

        final float c = 1.0f;
        if( res.x < -c )
            return false;
        if( res.y < -c )
            return false;
        
        src.set(v3.x-x, v3.y-y, v3.z, 1);
        Matrix4f.transform(camera.getViewProjectionMatrix4f(), src, res);

        if( res.x > c  )
            return false;
        if( res.y > c  )
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
    private Matrix4f tmp = new Matrix4f();
    private Matrix4f vpmatrix = new Matrix4f();
}

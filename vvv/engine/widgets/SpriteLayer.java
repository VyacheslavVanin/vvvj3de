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

    private final Vector4f res = new Vector4f();
    private final Vector4f src = new Vector4f();
    
    /**
     *  Determine if sprite in view
     * @param spr
     * @return 
     */
    private boolean isInView(Sprite spr)
    {
        /*
           ---(*)
           |   |
           | c |
           |   |
          (*)---
            Test is points marked with stars(*) in screen (-1,1 ; -1,1)
        */
        final float dx = spr.getScale().x * 0.5f; // from sprite center to left or right edge
        final float dy = spr.getScale().y * 0.5f; // from sprite center to top or bottom edge

        final Vector3f v3 = spr.getPosition();
        src.set(v3.x+dx, v3.y+dy, v3.z, 1);
        Matrix4f.transform( camera.getViewProjectionMatrix4f(), src, res);

        final float c = 1.0f;
        if( res.x < -c || res.y < -c)
            return false;
 
        src.set(v3.x-dx, v3.y-dy, v3.z, 1);
        Matrix4f.transform( camera.getViewProjectionMatrix4f(), src, res);

        return res.x <= c && res.y <= c;
    }
    
    @Override
    public void draw() throws Exception
    {
        if (shader == null)
        {
            throw new Exception("no assigned Shader to layer");
        }
        final List<GraphicObject> objects = getObjects();
        
        final Matrix4f vpmatrix = camera.getViewProjectionMatrix4f();
       
        final Geometry sg = SpriteGeometry.getGeometry();
        sg.activate();
        shader.activate();
         
        final int size = objects.size();
        for( int i = 0; i < size; ++i)
        {
            final GraphicObject go = objects.get(i);
            // all objects in list are instances of Sprite
            // (was checked onAddObject). So it's safe to cast.
            final Sprite spr = (Sprite) go;     
            
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
        final float h = getHeight();
        final float w = getWidth();
        camera.setOrtho(h / 2, -h / 2, -w / 2, w / 2, -2, 2);
      //  camera.setBodyForward(new Vec3(0, 0, 1), new Vec3(0, 1, 0));
     //   camera.setPos(0, 0, 0);
     //   camera.lookAt(0, 0, 1);
    }

    @Override
    protected boolean onAddObject(GraphicObject obj)
    {
        return obj instanceof Sprite;
    }


    public final void init()
    {
        initCamera();
    }

    private void initCamera()
    {  
        camera.setOrtho(1, -1, -1, 1, -1, 1);
        camera.setBodyForward(new Vec3(0, 0, 1), new Vec3(0, 1, 0));
        camera.lookAt(0, 0, 1);
    }

    public final Camera getCamera()
    {
        return camera;
    }

    public final void setShader(ModelShader shader)
    {
        this.shader = shader;
    }
      
    private final Camera camera = new Camera();
    private ModelShader shader = null;
    private final Matrix4f tmp = new Matrix4f();
}

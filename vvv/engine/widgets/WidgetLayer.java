package vvv.engine.widgets;

import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import vvv.engine.Camera;
import vvv.engine.shader.ModelShader;
import vvv.math.Vec3;

/**
 *
 * @author vvv
 */
public class WidgetLayer extends Layer
{
    private Camera camera = null;
    private ModelShader textShader = null;
    private ModelShader imageShader  = null;
    
    private final Matrix4f tmp = new Matrix4f();
    private Matrix4f vpmatrix = new Matrix4f();
    
    public  WidgetLayer()
    {
        super();
        init();
    }
        
    @Override
    public void draw() throws Exception 
    {
        List<GraphicObject> objects = getObjects();
        vpmatrix = camera.getViewProjectionMatrix4f();
        
        for(int i=0; i < objects.size(); ++i )
        {
            Widget wgt = (Widget)objects.get(i);
            wgt.draw();
        }
    }

    @Override
    public void onResize() 
    {
        float h = getHeight();
        float w = getWidth();
        camera.setOrtho(h , 0, 0, w , -2, 2);
        camera.setBodyForward(new Vec3(0, 0, 1), new Vec3(0, 1, 0));
        camera.setPos( 0, 0, -1);    
        camera.lookAt(0, 0, 0);
    }

    @Override
    protected boolean onAddObject(GraphicObject obj) 
    {
        if( obj instanceof Widget)
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
    
    public Camera getCamera()
    {
        return camera;
    }
    
    public ModelShader getTextShader()
    {
        return textShader;
    }
    
    public ModelShader getImageShader()
    {
        return imageShader;
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

    public void setTextShader( ModelShader shader)
    {
        this.textShader = shader;
    }
    
    public void setImageShader( ModelShader shader)
    {
        this.imageShader = shader;
    }
    
    public void onMouseMove( float x, float y )
    {
        final List<GraphicObject> objects = getObjects();
        final int numOjects = objects.size();
        for(int i=0; i < numOjects; ++i )
        {
            Widget wgt = (Widget)objects.get(i);
            if( wgt.isVisible() && wgt.isEnabled() )
            {
                if( wgt.invokeMouseMove(x, y) )
                {
                    break;
                }
            }
        }
    }
    
    public void onLeftMouseButtonDown( float x, float y) 
    {
        List<GraphicObject> objects = getObjects();
        for(int i=0; i < objects.size(); ++i )
        {
            Widget wgt = (Widget)objects.get(i);
            if( wgt.isVisible() && wgt.isEnabled() )
            {
                if( wgt.invokeLeftMouseButtonDown(x, y) )
                {
                    break;
                }
            }
        }
    }
    
    public void onLeftMouseButtonUp( float x, float y) 
    {
        List<GraphicObject> objects = getObjects();
        for(int i=0; i < objects.size(); ++i )
        {
            Widget wgt = (Widget)objects.get(i);
            if( wgt.isVisible() && wgt.isEnabled() )
            {
                if( wgt.invokeLeftMouseButtonUp(x, y) )
                {
                    break;
                }
            }
        }
    }
    
}

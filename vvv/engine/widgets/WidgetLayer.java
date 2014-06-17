package vvv.engine.widgets;

import java.util.List;
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
    private Widget      focusWidget = null;
    
    public  WidgetLayer()
    {
        super();
        init();
    }
        
    @Override
    public void draw() throws Exception 
    {
        final List<GraphicObject> objects = getObjects();
        
        final int size = objects.size();
        for(int i=0; i < size; ++i )
        {
            final Widget wgt = (Widget)objects.get(i);
            wgt.draw();
        }
    }

    @Override
    public void onResize() 
    {
        final float h = getHeight();
        final float w = getWidth();
        camera.setOrtho(h , 0, 0, w , -2, 2);
        camera.setBodyForward(new Vec3(0, 0, 1), new Vec3(0, 1, 0));
        camera.setPos( 0, 0, -1);    
        camera.lookAt(0, 0, 0);
    }

    @Override
    protected boolean onAddObject(GraphicObject obj) 
    {
        return obj instanceof Widget;
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
            final Widget wgt = (Widget)objects.get(i);
            if( wgt.isVisible() && wgt.isEnabled() )
            {
                if( wgt.invokeMouseMove(x, y) )
                {
                    break;
                }
            }
        }
    }
    
    public boolean onLeftMouseButtonDown( int button, float x, float y) 
    {
        final List<GraphicObject> objects = getObjects();
        final int size = objects.size();
        boolean ret = false;
        for(int i=0; i < size; ++i )
        {
            final Widget wgt = (Widget)objects.get(i);
            if( wgt.isVisible()  && wgt.isContainPoint(x, y))
            {
                ret = true;
                if( wgt.isEnabled() && wgt.invokeLeftMouseButtonDown(button, x, y) )
                {
                    break;
                }
            }
        }
        return ret;
    }
    
    public void onLeftMouseButtonUp( int button, float x, float y) 
    {
        final List<GraphicObject> objects = getObjects();
        final int size = objects.size();
        for(int i=0; i < size; ++i )
        {
            final Widget wgt = (Widget)objects.get(i);
            if( wgt.isVisible() && wgt.isEnabled() )
            {
                if( wgt.invokeLeftMouseButtonUp(button, x, y) )
                {
                    break;
                }
            }
        }
    }
    
    public final void setFocus(Widget wgt)
    {
        focusWidget = wgt;
    }
    
    public final Widget getFocus()
    {
        return focusWidget;
    }
    
}

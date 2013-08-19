/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import java.util.LinkedList;
import java.util.List;
import vvv.engine.Camera;
import vvv.engine.layers.GraphicObject;
import vvv.engine.layers.WidgetLayer;
import vvv.engine.shader.ModelShader;

/**
 *
 * @author Вячеслав
 */
public abstract class Widget extends GraphicObject
{
    private float width;
    private float height;
    private float xpos;
    private float ypos;
    private boolean visible = true;
    private Widget parent = null;
    private List<Widget> children = new LinkedList<>();
    
    public final float getWidth()  { return this.width; }
    public final float getHeight() { return this.height;}
    public final float getPosX()   { return this.xpos;  }
    public final float getPosY()   { return this.ypos;  }
    
    public final void  setWidth( float width)    { this.width = width; }
    public final void  setHeight( float height ) { this.height = height; }
    public final void  setPosX( float x)         { this.xpos = x; }
    public final void  setPosY( float y)         { this.ypos = y; }
    
    protected final float getGlobalPosX()
    {
        float  parentPosX;
        if( parent==null )
        {
            parentPosX = 0;
        }
        else
        {
            parentPosX = parent.getGlobalPosX();
        }
        return this.xpos + parentPosX;
    }
    
    protected final float getGlobalPosY()
    {
        float  parentPosY;
        if( parent==null )
        {
            parentPosY = 0;
        }
        else
        {
            parentPosY = parent.getGlobalPosY();
        }
        return this.xpos + parentPosY;
    }
    
    public final void draw() throws Exception
    {
        if( visible )
        {
            onDraw();
            for( Widget chld: children )
            {
               chld.draw();
            }
        }
    }
    
    @Override
    public WidgetLayer getLayer()
    {
        if( parent != null )
        {
            return parent.getLayer();
        }
        else
        {
            return (WidgetLayer)super.getLayer();
        }
    }
    
    protected final ModelShader getActiveShader()
    {
        return getLayer().getActiveShader();
    }
    
    protected final Camera getCamera()
    {
        return getLayer().getCamera();
    }
    
    public final void setVisible( boolean b)
    {
        this.visible = b;
    }
    
    public final boolean isVisible()
    {
        return this.visible;
    }
    
    public abstract void onDraw() throws Exception;
    
    public void onMouseMove( float x, float y) {}
    
    public void onMouseButtonDown( float x, float y) {}
    
    public void onMouseButtonUp( float x, float y) {}
 
}

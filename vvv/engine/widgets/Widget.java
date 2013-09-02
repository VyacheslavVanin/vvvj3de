/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import java.util.LinkedList;
import java.util.List;
import vvv.engine.Camera;
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
    protected Widget parent = null;
    private List<Widget> children = new LinkedList<>();
    
    public final float getWidth()  { return this.width; }
    public final float getHeight() { return this.height;}
    protected final float getPosX()   { return this.xpos;  }
    protected final float getPosY()   { return this.ypos;  }
    
    public final void  setWidth( float width)    { this.width = width; }
    public final void  setHeight( float height ) { this.height = height; }
    protected final void  setPosX( float x)         { this.xpos = x; }
    protected final void  setPosY( float y)         { this.ypos = y; }
    
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
        return this.ypos + parentPosY;
    }
    
    
   
    public final void setPosition( float x, float y)
    {
        this.setPosX(x);
        this.setPosY(y);
        onSetPosition( x, y);
    }
    
    public final void setSize( float width, float height)
    {
        setWidth(width);
        setHeight(height);
        onSetSize(width, height);
    }
    
    
    final void draw() throws Exception
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
    
    protected final ModelShader getTextShader()
    {
        return getLayer().getTextShader();
    }
    
    protected final ModelShader getImageShader()
    {
        return getLayer().getImageShader();
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
    
    public boolean isContainPoint( float x, float y)
    {
        float px = getGlobalPosX();
        float py = getGlobalPosY();
        float w  = getWidth();
        float h  = getHeight();
        
        if( x > px + w || x < px )
        {
            return false;
        }
        
        if( y > py + h || y < py )
        {
            return false;
        }
        
        return true;
    }
    
    protected boolean deleteChild( Widget child )
    {
        if( children.remove(child) )
        {
            child.parent = null;
        }
        else
        {
            return false;
        }
        return true;
    }
    
    protected boolean addChild( Widget child)
    {
        if( children.contains(child) )
        {
            return false;
        }
        
        if( child.parent != null)
        {
            child.parent.deleteChild(child);
        }
 
        children.add(child);
        child.parent = this;
        
        return true;
    }
    
    protected List<Widget> getChildren()
    {
        return children;
    }
    
    protected abstract void onDraw() throws Exception;
    protected abstract void onSetPosition(float x, float y);
    protected abstract void onSetSize( float w, float h);
    
            
     boolean onMouseMove( float x, float y) { return false;}
    
     boolean onLeftMouseButtonDown( float x, float y) { return false;}
    
     boolean onLeftMouseButtonUp( float x, float y) { return false;}

}

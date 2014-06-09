/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import vvv.engine.Camera;
import vvv.engine.Rect;
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
    
    private final Rect  clipArea = new Rect();
    
    private boolean visible = true;
    private boolean enabled = true;
    
    protected Widget parent = null;
    private final List<Widget> children = new ArrayList<>();
    protected final PositionProperties position = new PositionProperties();
    private final ListenerContainer onEnterListener = new ListenerContainer();
    private final ListenerContainer onLeaveListener = new ListenerContainer();
    private boolean inArea = false;
    
    
    public final float getWidth()  { return this.width; }
    public final float getHeight() { return this.height;}
    public final float getPosX()   { return this.xpos;  }
    public final float getPosY()   { return this.ypos;  }
    
    protected final void  setWidth( float width)    
    { 
        this.width = width;   
        clipArea.setWidth(width);
    }
    
    protected final void  setHeight( float height ) 
    { 
        this.height = height; 
        clipArea.setHeight(height);
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
    
    
    @Override
    public final WidgetLayer getLayer()
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
    
    
    public final void setVisible( boolean b)
    {
        this.visible = b;
    }
    
    public final boolean isVisible()
    {
        return this.visible;
    }
    
    public final void setEnabled(boolean b)
    {
        if( this.enabled != b )
        {
            if( b )
            {
                onEnable();
            }
            else
            {
                onDisable();
            }
            this.enabled = b;
            for(Widget w : children)
            {
                w.setEnabled(b);
            }
        }

    }
    
    public final boolean isEnabled()
    {
        return this.enabled;
    }
    
    
    
    public final void addOnEnterListener(ActionListener listener) {
        this.onEnterListener.addListener(listener);
    }

    public final void addOnLeaveListener(ActionListener listener) {
        this.onLeaveListener.addListener(listener);
    }

    public final void removeOnEnterListener(ActionListener listener) {
        this.onEnterListener.removeListener(listener);
    }

    public final void removeOnLeaveListener(ActionListener listener) {
        this.onLeaveListener.removeListener(listener);
    }


    public boolean isContainPoint( float x, float y)
    {
        final float px = getGlobalPosX();
        final float py = getGlobalPosY();
        final float w  = getWidth();
        final float h  = getHeight();
        
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
    
    
    final void draw() throws Exception
    {
        if( visible )
        {
            if( parent != null )
            {
                clipArea.set(getGlobalPosX(), getGlobalPosY(), getWidth(), getHeight());
                Rect.intersection(clipArea, parent.clipArea, clipArea);
            }
            else
            {
                clipArea.set(getGlobalPosX(), getGlobalPosY(), getWidth(), getHeight());
            }
            glEnable( GL_SCISSOR_TEST );
            glScissor( (int)clipArea.getLeft(),  (int)clipArea.getBottom(), 
                       (int)clipArea.getWidth(), (int)clipArea.getHeight() );
            onDraw();
            glDisable( GL_SCISSOR_TEST );
            
            final int size = children.size();
            for(int i =0; i < size; ++i)
            //for( Widget chld: children )
            {
               final Widget chld = children.get(i);
               chld.draw();
            }
        }
    }
        
    final boolean invokeMouseMove( float x, float y) 
    {
        if(enabled && visible)
        {
            onMouseMoveBase(x, y);
        }
        return false;
    }

    final boolean invokeLeftMouseButtonDown( float x, float y) 
    { 
        if( enabled && visible)
        {
            onLeftMouseButtonDown(x, y);
        }
        return false;
    }

    final boolean invokeLeftMouseButtonUp( float x, float y) 
    { 
        if( enabled && visible)
        {
            onLeftMouseButtonUp(x, y);
        }
        return false;
    }

 
    protected final void  setPosX( float x)      
    { 
        this.xpos = x; 
        clipArea.setLeft( getGlobalPosX());
    }
    
    protected final void  setPosY( float y)      
    { 
        this.ypos = y; 
        clipArea.setBottom( getGlobalPosY());
    }
    
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
   
    protected final boolean deleteChild( Widget child )
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
    
    protected final boolean addChild( Widget child)
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
    
    protected final List<Widget> getChildren()
    {
        return children;
    }
    
    protected final boolean isMouseInArea() 
    {
        return inArea;
    }
    
    
    protected void onSetPosition(float x, float y)
    {
        position.setPosition( (float)Math.floor(getGlobalPosX() ), 
                              (float)Math.floor(getGlobalPosY() ),
                              0);
    }
    
    protected abstract void onDraw() throws Exception;
   
    protected abstract void onSetSize( float w, float h);
    protected void onAttach() {};
    

    protected void onMouseMove(float x,float y) {}
    protected void onLeftMouseButtonDown( float x, float y) {}
    protected void onLeftMouseButtonUp(float x, float y) {}
    
    protected void onMouseEnter() {}
    protected void onMouseLeave() {}
    
    protected void onEnable()  {}
    protected void onDisable() {}
    
    
    private void onMouseMoveBase(float x, float y)
    {
        if( isContainPoint(x, y) )
        {
            if( !inArea )
            {
                inArea = true;
                onMouseEnterBase();
            }
        }
        else
        {
            if( inArea )
            {
                inArea = false;
                onMouseLeaveBase();
            }    
        }
        
        onMouseMove(x, y);
    }
    
    private void onMouseEnterBase()
    {  
        onMouseEnter();
        onEnterListener.action();
    }
    
    private void onMouseLeaveBase()
    {  
        onMouseLeave();
        onLeaveListener.action();
    }

}

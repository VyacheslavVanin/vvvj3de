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

/**
 *
 * @author Вячеслав
 */
public abstract class Widget extends GraphicObject
{
    private float width = 0;
    private float height = 0;
    private float local_position_x = 0;
    private float local_position_y = 0;
    
    private final Rect  clipArea = new Rect();
    
    private boolean visible = true;
    private boolean enabled = true;
    
    
    protected Widget parent = null;
    private final List<Widget> children = new ArrayList<>();
    protected final PositionProperties position = new PositionProperties();
    private final ListenerContainer onEnterListener = new ListenerContainer();
    private final ListenerContainer onLeaveListener = new ListenerContainer();
    private boolean inArea = false;
    
    /**
     * @return Return widget width in pixels*/
    public final float getWidth()  { return this.width; }
    
    /**
     * @return Widget height in pixels */
    public final float getHeight() { return this.height;}
    
    /**
     * @brief Return local widget position respective to parent widget. 
     *        <br>If no parent widget then equals layer width
     * @return horizontal component of position in pixels */
    public final float getPosX()   { return this.local_position_x;  }
    
    /**
     * @brief Return local widget position respective to parent widget. 
     *        <br>If no parent widget then equals layer width
     * @return vertical component of position in pixels */
    public final float getPosY()   { return this.local_position_y;  }
    
    /**
     * @brief Set width of widget.
     * @param width Width of widget in pixels, should be non-negative value */
    protected final void  setWidth( float width)    
    {
        if( width < 0 )
        {
            throw new IllegalArgumentException( "width must be >= 0" );
        }
        this.width =  width;   
        clipArea.setWidth( this.width );
    }
    
    /**
     * @brief Set height of widget.
     * @param height Height of widget in pixels, should be non-negative value */
    protected final void  setHeight( float height ) 
    { 
        if( height < 0 )
        {
            throw new IllegalArgumentException( "height must be >= 0" );
        }
        this.height = height; 
        clipArea.setHeight( this.height );
    }
    
    /**
     * @brief Set local position of widget on parent widget  
     * @param x - horizontal component
     * @param y - vertical component */
    public final void setPosition( float x, float y)
    {
        this.setPosX(x);
        this.setPosY(y);
        onSetPosition( x, y);
    }
    
    /**
     * @brief Set size of widget. Invokes onSetSize method.
     * @param width Width in pixels
     * @param height Height in pixels
     */
    public final void setSize( float width, float height)
    {
        setWidth(width);
        setHeight(height);
        onSetSize(width, height);
    }
    
    /**
     * @return WidgetLayer of widget 
     */    
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
    
    /**
     * @brief Set visibility flag
     * @param b - if true widget visible, else hide widget */
    public final void setVisible( boolean b)
    {
        this.visible = b;
    }
    
    /**
     * @brief Get visibility flag
     * @return visibility flag  */
    public final boolean isVisible()
    {
        return this.visible;
    }
    
    /**
     * @brief Set enabling flag of widget
     * @param b - if true set widget to enabled state, else to disabled  */
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
    
    /**
     * @brief Get enable state of widget
     * @return if true - widget enabled, if false - disabled */
    public final boolean isEnabled()
    {
        return this.enabled;
    }
    
    /**
     * @brief Add ActionListener on MouseEnter-event  to widget
     * @param listener  */
    public final void addOnEnterListener(ActionListener listener) {
        this.onEnterListener.addListener(listener);
    }

    /**
     * @brief Add ActionListener on MouseLeave-event to widget
     * @param listener  */
    public final void addOnLeaveListener(ActionListener listener) {
        this.onLeaveListener.addListener(listener);
    }

    /**
     * @brief Remove listener from widget
     * @param listener */
    public final void removeOnEnterListener(ActionListener listener) {
        this.onEnterListener.removeListener(listener);
    }

    /**
     * @brief Remove listener from widget
     * @param listener */
    public final void removeOnLeaveListener(ActionListener listener) {
        this.onLeaveListener.removeListener(listener);
    }

   /**
    * @brief Check if widget contain point. (0,0 - bottom-left corner)
    * @param x - x component of point in screen pixels
    * @param y - y component of point in screen pixels
    * @return true if contain or false if not */
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
    
    private void updateClipArea()
    {
        if (parent != null) {
            clipArea.set(getGlobalPosX(), getGlobalPosY(), getWidth(), getHeight());
            Rect.intersection(clipArea, parent.clipArea, clipArea); // clipArea must match parent clipArea
        }
        else {
            clipArea.set(getGlobalPosX(), getGlobalPosY(), getWidth(), getHeight());
        }
    }
    
    final void draw() throws Exception
    {
        if( visible )
        {
            updateClipArea();
            glEnable( GL_SCISSOR_TEST );
            glScissor( (int)clipArea.getLeft(),  (int)clipArea.getBottom(), 
                       (int)clipArea.getWidth(), (int)clipArea.getHeight() );
            onDraw(); // Call user defined behavior on draw
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
        
    /**
     * @brief Notify widget about MouseMove-event.
     * @param x - Mouse position x component in pixels (0,0 is in bottom-left corner of screen)
     * @param y - Mouse position y component in pixels 
     * @return  */
    final boolean invokeMouseMove( float x, float y) 
    {
        if(enabled && visible)
        {
            onMouseMoveBase(x, y);
        }
        return false;
    }

    /**
     * @brief Notify widget about MouseButtonDown-event.
     * @param button - button pressed ( 0 - left, 1 -right, 2 - middle and so on)
     * @param x - Mouse position x component in pixels (0,0 is in bottom-left corner of screen)
     * @param y - Mouse position y component in pixels 
     * @return  */
    final boolean invokeMouseButtonDown( int button, float x, float y) 
    { 
        if( enabled && visible )
        {
            final WidgetLayer layer = getLayer();
            layer.setFocus(this);
            onMouseButtonDown( button, x, y);
        }
        return false;
    }

    /**
     * @brief Notify widget about MouseButtonUp-event.
     * @param button - button released ( 0 - left, 1 -right, 2 - middle and so on)
     * @param x - Mouse position x component in pixels (0,0 is in bottom-left corner of screen)
     * @param y - Mouse position y component in pixels 
     * @return  */
    final boolean invokeMouseButtonUp( int button, float x, float y) 
    { 
        if( enabled && visible)
        {
            onMouseButtonUp( button, x, y);
        }
        return false;
    }

    final void invokeKeyPress( int key, char character)
    {
        onKeyPress(key, character);
    }
 
    protected final void  setPosX( float x)      
    { 
        this.local_position_x = x; 
    }
    
    protected final void  setPosY( float y)      
    { 
        this.local_position_y = y; 
    }
    
    /**
     * @brief Get real position of widget in screen coordinates (0,0 - bottom-left corner) 
     * @return x component of widget position */
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
        return this.local_position_x + parentPosX;
    }
    
    /**
     * @brief Get real position of widget in screen coordinates (0,0 - bottom-left corner) 
     * @return y component of widget position */
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
        return this.local_position_y + parentPosY;
    }
     
    protected final Camera getCamera()
    {
        return getLayer().getCamera();
    }
   
    /**
     * @brief Remove child widget 
     * @param child
     * @return true if removed, false if there was not such child widget on this widget */
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
    
    /**
     * @brief Add child Widget to current widget
     * @param child
     * @return true if added, false if child already in list of children */
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
        child.onAttach();
        
        return true;
    }
    
    /**
     * @brief Get list of child widgets attached to current widget
     * @return list of child widgets */
    protected final List<Widget> getChildren()
    {
        return children;
    }
    
    /**
     * @brief return if mouse in Area of widget
     * @return */
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
    
    /**
     * Method called after widget was attached to new parent  */
    protected void onAttach() {};
    

    protected void onMouseMove(float x,float y) {}
    protected void onMouseButtonDown( int button, float x, float y) {}
    protected void onMouseButtonUp(int button, float x, float y) {}
    
    protected void onMouseEnter() {}
    protected void onMouseLeave() {}
    
    protected void onEnable()  {}
    protected void onDisable() {}
    
    protected void onGetFocus() {}
    protected void onLooseFocus() {}
    
    protected void onKeyPress( int key, char character){}
    
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

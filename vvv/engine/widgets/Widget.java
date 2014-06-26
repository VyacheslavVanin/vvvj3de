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
    private int width = 0;
    private int height = 0;
    private int local_position_x = 0;
    private int local_position_y = 0;
    
    private final Rect  clipArea = new Rect();
    
    private boolean visible = true;
    private boolean enabled = true;
    private boolean focusable = true;
    
    protected Widget parent = null;
    private final List<Widget> children = new ArrayList<>();
    protected final PositionProperties position = new PositionProperties();
    private final ListenerContainer onMouseEnterListener = new ListenerContainer();
    private final ListenerContainer onMouseLeaveListener = new ListenerContainer();
    private boolean inArea = false;
    
    /**
     * @return Return widget width in pixels*/
    public final int getWidth()  { return this.width; }
    
    /**
     * @return Widget height in pixels */
    public final int getHeight() { return this.height;}
    
    /**
     *  Return local widget position respective to parent widget. 
     *        <br>If no parent widget then equals layer width
     * @return horizontal component of position in pixels */
    public final int getPosX()   { return this.local_position_x;  }
    
    /**
     *  Return local widget position respective to parent widget. 
     *        <br>If no parent widget then equals layer width
     * @return vertical component of position in pixels */
    public final int getPosY()   { return this.local_position_y;  }
    
    /**
     *  Set width of widget.
     * @param width Width of widget in pixels, should be non-negative value */
    protected final void  setWidth( int width)    
    {
        if( width < 0 )
        {
            throw new IllegalArgumentException( "width must be >= 0" );
        }
        this.width =  width;   
        clipArea.setWidth( this.width );
    }
    
    /**
     *  Set height of widget.
     * @param height Height of widget in pixels, should be non-negative value */
    protected final void  setHeight( int height ) 
    { 
        if( height < 0 )
        {
            throw new IllegalArgumentException( "height must be >= 0" );
        }
        this.height = height; 
        clipArea.setHeight( this.height );
    }
    
    /**
     *  Set local position of widget on parent widget  
     * @param x - horizontal component
     * @param y - vertical component */
    public final void setPosition( int x, int y)
    {
        this.setPosX(x);
        this.setPosY(y);
        onSetPosition( x, y);
        onRefresh();
    }
    
    /**
     *  Set size of widget. Invokes onSetSize method.
     * @param width Width in pixels
     * @param height Height in pixels
     */
    public final void setSize( int width, int height)
    {
        setWidth(width);
        setHeight(height);
        onSetSize(width, height);
        onRefresh();
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
     *  Set visibility flag
     * @param b - if true widget visible, else hide widget */
    public final void setVisible( boolean b)
    {
        this.visible = b;
    }
    
    /**
     *  Get visibility flag
     * @return visibility flag  */
    public final boolean isVisible()
    {
        return this.visible;
    }
    
    /**
     *  Set enabling flag of widget
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
     *  Get enable state of widget
     * @return if true - widget enabled, if false - disabled */
    public final boolean isEnabled()
    {
        return this.enabled;
    }
    
    /**
     *  Add ActionListener on MouseEnter-event  to widget
     * @param listener  */
    public final void addOnMouseEnterListener(ActionListener listener) {
        this.onMouseEnterListener.addListener(listener);
    }

    /**
     *  Add ActionListener on MouseLeave-event to widget
     * @param listener  */
    public final void addOnMouseLeaveListener(ActionListener listener) {
        this.onMouseLeaveListener.addListener(listener);
    }

    /**
     *  Remove listener from widget
     * @param listener */
    public final void removeOnMouseEnterListener(ActionListener listener) {
        this.onMouseEnterListener.removeListener(listener);
    }

    /**
     * Remove listener from widget
     * @param listener */
    public final void removeOnMouseLeaveListener(ActionListener listener) {
        this.onMouseLeaveListener.removeListener(listener);
    }

   /**
    * Check if widget contain point. (0,0 - bottom-left corner)
    * @param x - x component of point in screen pixels
    * @param y - y component of point in screen pixels
    * @return true if contain or false if not */
    public boolean isContainPoint( int x, int y)
    {
        final int px = getGlobalPosX();
        final int py = getGlobalPosY();
        final int w  = getWidth();
        final int h  = getHeight();
        
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
        clipArea.set(getGlobalPosX(), getGlobalPosY(), getWidth(), getHeight());
        if (parent != null) {
            Rect.intersection(clipArea, parent.clipArea, clipArea); // clipArea must match parent clipArea
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
     * Notify widget about MouseMove-event.
     * @param x - Mouse position x component in pixels (0,0 is in bottom-left corner of screen)
     * @param y - Mouse position y component in pixels 
     * @return  */
    final boolean invokeMouseMove( int x, int y) 
    {
        if(enabled && visible)
        {
            onMouseMoveBase(x, y);
        }
        return false;
    }

    /**
     * Notify widget about MouseButtonDown-event.
     * @param button - button pressed ( 0 - left, 1 -right, 2 - middle and so on)
     * @param x - Mouse position x component in pixels (0,0 is in bottom-left corner of screen)
     * @param y - Mouse position y component in pixels 
     * @return  */
    final boolean invokeMouseButtonDown( int button, int x, int y) 
    { 
        if( enabled && visible )
        {
            if( isFocusable() )
            {
                final WidgetLayer layer = getLayer();
                layer.setFocus(this);
            }
            return onMouseButtonDown( button, x, y);
        }
        return false;
    }

    /**
     * Notify widget about MouseButtonUp-event.
     * @param button - button released ( 0 - left, 1 -right, 2 - middle and so on)
     * @param x - Mouse position x component in pixels (0,0 is in bottom-left corner of screen)
     * @param y - Mouse position y component in pixels 
     * @return  */
    final boolean invokeMouseButtonUp( int button, int x, int y) 
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
 
    protected final void  setPosX( int x)      
    { 
        this.local_position_x = x; 
    }
    
    protected final void  setPosY( int y)      
    { 
        this.local_position_y = y; 
    }
    
    /**
     * Get real position of widget in screen coordinates (0,0 - bottom-left corner) 
     * @return x component of widget position */
    protected final int getGlobalPosX()
    {
        int  parentPosX;
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
     * Get real position of widget in screen coordinates (0,0 - bottom-left corner) 
     * @return y component of widget position */
    protected final int getGlobalPosY()
    {
        int  parentPosY;
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
     * Remove child widget 
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
     * Add child Widget to current widget
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
     * Get list of child widgets attached to current widget
     * @return list of child widgets */
    protected final List<Widget> getChildren()
    {
        return children;
    }
    
    /**
     * Return if mouse in Area of widget
     * @return */
    protected final boolean isMouseInArea() 
    {
        return inArea;
    }
    
    
    protected void onSetPosition(float x, float y)
    {
        position.setPosition( (float)(getGlobalPosX() ), 
                              (float)(getGlobalPosY() ),
                              0);
    }
    
    protected abstract void onDraw() throws Exception;
   
    protected void onSetSize( int w, int h) {};
    
    /**
     * Method called after widget changed its position or size.
     * <br> Can be used to rearrange children widgets */
    protected abstract void onRefresh();
    
    /**
     * Method called after widget was attached to new parent  */
    protected void onAttach() {};
    

    protected void onMouseMove(int x, int y) {}
    protected boolean onMouseButtonDown( int button, int x, int y) { return true;}
    protected void onMouseButtonUp(int button, int x, int y) {}
    
    protected void onMouseEnter() {}
    protected void onMouseLeave() {}
    
    protected void onEnable()  {}
    protected void onDisable() {}
    
    protected void onGetFocus() {}
    protected void onLooseFocus() {}
    
    protected void onKeyPress( int key, char character){}
    
    private void onMouseMoveBase(int x, int y)
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
        onMouseEnterListener.action();
    }
    
    private void onMouseLeaveBase()
    {  
        onMouseLeave();
        onMouseLeaveListener.action();
    }

    /**
     * @return the focusable
     */
    public boolean isFocusable() {
        return focusable;
    }

    /**
     * @param focusable the focusable to set
     */
    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

}

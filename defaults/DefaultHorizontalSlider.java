/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package defaults;

import vvv.engine.widgets.AbstractSlider;
import vvv.engine.widgets.ColorRectangleWidget;

/**
 *
 * @author vvv
 */
public class DefaultHorizontalSlider extends AbstractSlider
{
    private int handle_width = 30;
    private final int HANDLE_WIDTH_MIN = 0;
    private final ColorRectangleWidget leftPart   = new ColorRectangleWidget();
    private final ColorRectangleWidget centerPart = new ColorRectangleWidget();
    private final ColorRectangleWidget rightPart  = new ColorRectangleWidget();    
    
    public DefaultHorizontalSlider(int range)
    {
        super(range);
        centerPart.setColor( DefaultSystemColors.getControlColor());
        leftPart.setColor( DefaultSystemColors.getSliderActivePartColor() );
        rightPart.setColor( DefaultSystemColors.getSliderPassivePartColor());
        addChild(leftPart);
        addChild(rightPart);
        addChild(centerPart);
        leftPart.setFocusable( false );
        rightPart.setFocusable( false );
        centerPart.setFocusable( false );
        
        setSize(200, 16);
    }
    
    public DefaultHorizontalSlider()
    {
        this(100);
    }
    

    @Override
    protected void onDraw() throws Exception 
    {  
    }

    @Override
    protected void onSetSize(int w, int h) 
    {
        calcParts();
    }
    
    @Override
    protected  void onSetPosition(float x, float y)
    {
        super.onSetPosition(x, y);
        calcParts();
    }
    
    @Override
    protected void onMouseDrag(float x, float y)
    {
        final float xpos = getGlobalPosX();
        final float dx = Math.max( x - xpos, 0 );
        final float width = getWidth();
        setValue(  (int) ((dx / width) * getRange()));        
    }
    
    @Override
    protected  void onSetValue(int value)
    {
        calcParts();
    }
  
    private void  calcParts()
    {
        final int value = getValue();
        final int range = getRange();
        final int widgetWidth = getWidth();
        final int widgetHeight= getHeight();
        
        final int centerWidth = Math.min( handle_width, widgetWidth );
        final int centerOffset= (int)((widgetWidth - centerWidth) * ( (float)value/range )) ; 
        final int leftWidth = centerOffset;
        final int leftOffset = 0;
        
        final int rightWidth  = widgetWidth - (centerWidth + leftWidth);
        final int rightOffset = centerOffset + centerWidth;
        
        System.out.println( String.format("ww: %d, lw: %d, cw: %d, rw: %d\n", 
                                          widgetWidth, leftWidth, centerWidth, rightWidth));
        System.out.println( String.format("ww: %d, lo: %d, co: %d, ro: %d\n", 
                                          widgetWidth, leftOffset, centerOffset, rightOffset));
        
        leftPart.setPosition( leftOffset, 0);
        leftPart.setSize(leftWidth, widgetHeight);
        
        centerPart.setPosition(centerOffset, 0);
        centerPart.setSize(centerWidth, widgetHeight);
        
        rightPart.setPosition( rightOffset, 0);
        rightPart.setSize( rightWidth, widgetHeight);
    }
    
    public void setHandleWidth( int width)
    {
        handle_width = Math.max( Math.min(width, getWidth()/2), HANDLE_WIDTH_MIN);
        calcParts();
    }
}

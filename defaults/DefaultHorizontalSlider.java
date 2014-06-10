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

    public DefaultHorizontalSlider(int range)
    {
        super(range);
        centerPart.setColor( DefaultSystemColors.getControlColor());
        leftPart.setColor( DefaultSystemColors.getSliderActivePartColor() );
        rightPart.setColor( DefaultSystemColors.getSliderPassivePartColor());
        addChild(leftPart);
        addChild(rightPart);
        addChild(centerPart);
          
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
    protected void onSetSize(float w, float h) 
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
        calcParts();
    }

    private final ColorRectangleWidget leftPart   = new ColorRectangleWidget();
    private final ColorRectangleWidget centerPart = new ColorRectangleWidget();
    private final ColorRectangleWidget rightPart  = new ColorRectangleWidget();
    
    private void  calcParts()
    {
        final int value = getValue();
        final int range = getRange();
        final float widgetWidth = getWidth();
        final float widgetHeight= getHeight();
        
        final float centerWidth = Math.min( DEFAULT_CENTER_WIDTH, widgetWidth );
        final float centerOffset= (widgetWidth - centerWidth) * ( (float)value/range ); 
        final float leftWidth = centerOffset;
        final float leftOffset = 0;
        
        final float rightWidth  = widgetWidth - (centerWidth + leftWidth);
        final float rightOffset = centerOffset + centerWidth;
        
        leftPart.setPosition( leftOffset, 0);
        leftPart.setSize(leftWidth, widgetHeight);
        
        centerPart.setPosition(centerOffset, 0);
        centerPart.setSize(centerWidth, widgetHeight);
        
        rightPart.setPosition( rightOffset, 0);
        rightPart.setSize( rightWidth, widgetHeight);
    }
    
    private final int DEFAULT_CENTER_WIDTH = 30;
    
}

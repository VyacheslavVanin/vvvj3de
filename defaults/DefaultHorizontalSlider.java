/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package defaults;

import org.lwjgl.util.vector.Matrix4f;
import vvv.engine.Camera;
import vvv.engine.Geometry;
import vvv.engine.colorRectangle.RectangleGeometry;
import vvv.engine.colorRectangle.SolidColorRectangle;
import vvv.engine.shader.ModelShader;
import vvv.engine.widgets.AbstractSlider;

/**
 *
 * @author vvv
 */
public class DefaultHorizontalSlider extends AbstractSlider
{

    public DefaultHorizontalSlider(int range)
    {
        super(range);
        center.setColor( DefaultSystemColors.getControlColor());
        leftPart.setColor( DefaultSystemColors.getSliderActivePartColor() );
        rightPart.setColor( DefaultSystemColors.getSliderPassivePartColor());
        
        setSize(200, 24);
    }
    
    public DefaultHorizontalSlider()
    {
        this(100);
    }
    
    private static final Matrix4f tmp = new Matrix4f();

    @Override
    protected void onDraw() throws Exception 
    {
        ModelShader shader = Defaults.getSolidColorShader();
        Camera cam = getCamera();
        shader.activate();
        Geometry geometry = RectangleGeometry.getGeometry();
        geometry.activate();
        
        
        shader.setColor( 0, leftPart.getColor() );
                Matrix4f.mul( cam.getViewProjectionMatrix4f(),
                              leftPart.getMatrix4f(),
                              tmp);
        shader.setModelViewProjectionMatrix(tmp);
        geometry.draw();
        
        shader.setColor( 0, center.getColor() );
                Matrix4f.mul( cam.getViewProjectionMatrix4f(),
                              center.getMatrix4f(),
                              tmp);
        shader.setModelViewProjectionMatrix(tmp);
        geometry.draw();
        
  
        shader.setColor( 0, rightPart.getColor() );
                Matrix4f.mul( cam.getViewProjectionMatrix4f(),
                              rightPart.getMatrix4f(),
                              tmp);
        shader.setModelViewProjectionMatrix(tmp);
        geometry.draw();
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
        final float dx = Math.max( x - getGlobalPosX(), 0 );
        final float width = getWidth();
        setValue(  (int) ((dx / width) * getRange()));
        calcParts();
        
    }
    
    private final SolidColorRectangle leftPart = new SolidColorRectangle();
    private final SolidColorRectangle rightPart = new SolidColorRectangle();
    private final SolidColorRectangle center = new SolidColorRectangle();
    
    private void  calcParts()
    {
        final int value = getValue();
        final int range = getRange();
        final float widgetWidth = getWidth();
        final float widgetHeight= getHeight();
        final float globalPosX  = getGlobalPosX();
        final float offsetY  = getGlobalPosY() + widgetHeight;
        
        final float centerWidth = Math.min( DEFAULT_CENTER_WIDTH, widgetWidth );
        final float centerOffset= (widgetWidth - centerWidth) * ( (float)value/range ) + centerWidth/2;
        
        final float leftWidth = centerOffset - centerWidth/2;
        final float leftOffset = leftWidth / 2;
        
        
        final float rightWidth  = widgetWidth - (centerWidth + leftWidth);
        final float rightOffset = centerOffset + centerWidth/2 + rightWidth/2;
        
        leftPart.setScale( leftWidth, widgetHeight, 1);
        leftPart.setPosition( globalPosX +leftOffset, offsetY, 0 );
        
        center.setScale( centerWidth, widgetHeight, 1);
        center.setPosition( globalPosX + centerOffset, offsetY, 0);
        
        rightPart.setScale( rightWidth, widgetHeight, 1);
        rightPart.setPosition( globalPosX + rightOffset, offsetY, 0);
    }
    
    private final int DEFAULT_CENTER_WIDTH = 30;
    
}

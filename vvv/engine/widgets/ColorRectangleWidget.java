/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vvv.engine.widgets;

import org.lwjgl.util.vector.Matrix4f;
import vvv.engine.Camera;
import vvv.engine.Color;
import vvv.engine.ConstColor;
import vvv.engine.Geometry;
import vvv.engine.VariableColor;
import vvv.engine.colorRectangle.RectangleGeometry;
import vvv.engine.shader.ModelShader;

/**
 *
 * @author vvv
 */
public class ColorRectangleWidget extends Widget
{
    private Color color = new VariableColor( ConstColor.WHITE );
    
    public ColorRectangleWidget()
    {
    }
    
    public ColorRectangleWidget(Color color)
    {
        setColor(color);
    }
    
    public final void setColor( Color color )
    {
        this.color = color;
    }
    
    public final Color getColor()
    {
        return color;
    }
    
    @Override
    protected void onDraw() throws Exception 
    {
        final ModelShader shader = defaults.Defaults.getSolidColorShader();
        final Camera      camera = getCamera();
        final Geometry  geometry = RectangleGeometry.getGeometry();
        
        shader.activate();
        geometry.activate();
                    Matrix4f.mul( camera.getViewProjectionMatrix4f(), 
                                  position.getMatrix4f(), 
                                  tmp);
        shader.setModelViewProjectionMatrix(tmp);
        shader.setColor(0, color);
        defaults.Defaults.enableTransparency();
        geometry.draw();
        defaults.Defaults.disableTransparency();
        
    }
    private static final Matrix4f tmp = new Matrix4f();

    
    private void repositionBySize()
    {
        position.setPosition(   (float) Math.floor(getGlobalPosX() + getWidth()  / 2 ),
                                (float) Math.floor(getGlobalPosY() + getHeight() / 2 ),
                                0);
    }
    
    @Override
    protected void onSetSize(float w, float h) 
    {
        position.setScale(w, h, 1);
        repositionBySize();
    }
    
    @Override
    protected  void onSetPosition(float x, float y)
    {
        repositionBySize();
    }
}

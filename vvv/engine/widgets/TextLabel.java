/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import vvv.engine.Camera;
import vvv.engine.layers.PositionProperties;
import vvv.engine.shader.ModelShader;
import vvv.engine.text.Font;
import vvv.engine.text.TextLine;

/**
 *
 * @author vvv
 */
public class TextLabel extends Widget
{
    private TextLine textLine = null;
    private PositionProperties position = new PositionProperties();
    private Vector4f textColor = new Vector4f();
    private VerticalAlign   vAlign;
    private HorizontalAlign hAlign;

    public TextLabel()
    {
        this("");    
    }
    
    public TextLabel( String text)
    {   
        textLine = new TextLine();
        this.vAlign = VerticalAlign.CENTER;
        this.hAlign = HorizontalAlign.CENTER;
        this.setText(text); 
    }
    
    public final void setVerticalAlign( VerticalAlign va )
    {
        this.vAlign = va;
    }
    
    public final void setHorizontalAlign( HorizontalAlign ha )
    {
        this.hAlign = ha;
    }
    
    public final void setColor(float r, float g, float b, float a)
    {
        textColor.x = r;
        textColor.y = g;
        textColor.z = b;
        textColor.w = a;
    }
    
    public final void setText( String text )
    {
        textLine.setText(text);
    }
    
    public final void setFont( Font font )
    {
        textLine.setFont(font);
    }
    
    
    public final void setHeightByContents()
    {
        float h = textLine.getAscenderHight() - textLine.getDescenderHight();
        setHeight( h );
    }
    
    public final void setWidthByContext()
    {
        float w = textLine.getLineWidth();
        setWidth(w);
    }
    
    public final void setSizeByContents()
    {
        setHeightByContents();
        setWidthByContext();
    }
    
    
    public final void setPosition( float x, float y)
    {
        this.setPosition(x, y, 0);
    }
    
    public final void setPosition( float x, float y, float z)
    {
        this.setPosX(x);
        this.setPosY(y);
        float alignOffsetX = 0;
        float alignOffsetY = 0;
        switch( vAlign )
        {
            case BOTTOM:
                alignOffsetY = -textLine.getDescenderHight();
                break;
            case CENTER:
                {
                    float textHeight = textLine.getAscenderHight() - 
                                       textLine.getDescenderHight();
                    float descHeigt = (getHeight() - textHeight) / 2;
                    
                    alignOffsetY = descHeigt - textLine.getDescenderHight();
                }
                break;
            case TOP:
                alignOffsetY = getHeight() - textLine.getAscenderHight();
                break;
        }
        
        switch( hAlign )
        {
            case LEFT:
                alignOffsetX = 0;
                break;
            case CENTER:
                {
                    alignOffsetX = getWidth() / 2 -
                                   textLine.getLineWidth() / 2;
                }
                break;
            case RIGHT:
                alignOffsetX = getWidth() - textLine.getLineWidth();
                break;
        }
        
        position.setPosition( getGlobalPosX() + alignOffsetX,
                              getGlobalPosY() + alignOffsetY,
                              z );
    }
     
    
    @Override
    public void onDraw() throws Exception
    {
        ModelShader shader  = getTextShader();
        Camera      cam     = getCamera();   

        shader.activate();
        shader.setColor(0, textColor);
            Matrix4f.mul( cam.getViewProjectionMatrix4f(), 
                          position.getMatrix4f(), 
                          tmp);
        shader.setModelViewProjectionMatrix(tmp);
        shader.setTexture( 0, textLine.getTexture() );
        textLine.draw();
    }   
    private static  Matrix4f tmp = new Matrix4f();
  
    
}

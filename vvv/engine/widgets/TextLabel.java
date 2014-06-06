/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import defaults.DefaultSystemColors;
import defaults.Defaults;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.vector.Matrix4f;
import vvv.engine.Camera;
import vvv.engine.Color;
import vvv.engine.ConstColor;
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
    private Color textColor = DefaultSystemColors.getTextColor();
    private VerticalAlign   vAlign;
    private HorizontalAlign hAlign;
    private boolean         autosize = true;
    
    public TextLabel()
    {
        this("");    
    }
    
    public TextLabel( String text)
    {     
        textLine = new TextLine();
        try
        {
            textLine.setFont(Defaults.getFont());
        }
        catch(IOException ex)
        {
            Logger.getLogger(TextLabel.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        textColor = new ConstColor(r, g, b, a);
    }
    
    public final void setColor( Color c)
    {
        textColor = c;
    }
    
    public final void setText( String text )
    {
        textLine.setText(text);
        if( autosize )
        {
            setSizeByContents();
        }
    }
    
    public final void setFont( Font font )
    {
        textLine.setFont(font);
        if( autosize )
        {
            setSizeByContents();
        }
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
       
    public final void setAutoSize( boolean b )
    {
        this.autosize = b;
    }
    
    @Override
    public final void onSetPosition( float x, float y)
    {
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
                    alignOffsetX = (getWidth() - textLine.getLineWidth()) / 2;
                }
                break;
            case RIGHT:
                alignOffsetX = getWidth() - textLine.getLineWidth();
                break;
        }
        
        position.setPosition( (float) Math.floor( getGlobalPosX() + alignOffsetX ),
                              (float) Math.floor( getGlobalPosY() + alignOffsetY ),
                              0 );
    }
 
       
    @Override
    protected void onDraw() throws Exception
    {
        ModelShader shader  = getTextShader();
        Camera      cam     = getCamera();   

        shader.activate();
        shader.setColor(0, textColor.getVector());
            Matrix4f.mul( cam.getViewProjectionMatrix4f(), 
                          position.getMatrix4f(), 
                          tmp);
        shader.setModelViewProjectionMatrix(tmp);
        shader.setTexture( 0, textLine.getTexture() );
        textLine.draw();
    }   
    private static  Matrix4f tmp = new Matrix4f();

    @Override
    protected void onSetSize(float w, float h)
    {
        onSetPosition( getPosX(), getPosY() );
    }
}

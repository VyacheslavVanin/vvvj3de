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
    private final TextLine textLine;
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
        setFocusable(false);
    }
    
    public final void setVerticalAlign( VerticalAlign va )
    {
        this.vAlign = va;
    }
    
    public final void setHorizontalAlign( HorizontalAlign ha )
    {
        this.hAlign = ha;
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
    
    public final String getText()
    {
        return textLine.getText();
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
        int h = textLine.getAscenderHight() - textLine.getDescenderHight();
        setHeight( h );
    }
    
    public final void setWidthByContext()
    {
        int w = textLine.getLineWidth();
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
    protected void onDraw() throws Exception
    {
        final ModelShader shader  = Defaults.getTextShader();
        final Camera      cam     = getCamera();   

        shader.activate();
        shader.setColor(0, textColor);
            Matrix4f.mul( cam.getViewProjectionMatrix4f(), 
                          position.getMatrix4f(), 
                          tmp);
        shader.setModelViewProjectionMatrix(tmp);
        shader.setTexture( 0, textLine.getTexture() );
        
       
        textLine.draw();
        
    }   
    private static final Matrix4f tmp = new Matrix4f();

    @Override
    protected void onSetSize(int w, int h)
    {
        autosize = false;
    }

    @Override
    protected void onRefresh() 
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
}

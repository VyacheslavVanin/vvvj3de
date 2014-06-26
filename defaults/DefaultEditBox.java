/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import org.lwjgl.input.Keyboard;
import vvv.engine.Color;
import vvv.engine.ConstColor;
import vvv.engine.Globals;
import vvv.engine.text.Font;
import vvv.engine.widgets.ActionListener;
import vvv.engine.widgets.ColorRectangleWidget;
import vvv.engine.widgets.HorizontalAlign;
import vvv.engine.widgets.ListenerContainer;
import vvv.engine.widgets.TextLabel;
import vvv.engine.widgets.Widget;

/**
 *
 * @author vvv
 */
public class DefaultEditBox extends Widget {
    /* "leftPart"{cursor}"rightPart" */

    private final TextLabel leftPart = new TextLabel("");
    private final TextLabel rightPart = new TextLabel("");
    private final ColorRectangleWidget background = new ColorRectangleWidget( ConstColor.WHITE );
    private final ColorRectangleWidget cursor = new ColorRectangleWidget( ConstColor.BLACK );
    private  boolean  inFocus = false;
    
    private static final int cursorMargin = 3;
    private HorizontalAlign align = HorizontalAlign.LEFT;
    private final ListenerContainer onPressEnterListeners = new ListenerContainer();

    public DefaultEditBox() 
    {
        leftPart.setText("");
        rightPart.setText("");

        addChild( background );
        addChild(leftPart);
        addChild(rightPart);
        addChild( cursor );
        
        setSize( 200, leftPart.getHeight() + 6);
        
        background.setFocusable(false);
        background.setSize(200, 30);
        
        cursor.setSize( 1, leftPart.getHeight());
        cursor.setVisible(false);
    }

    public String getText() {
        return leftPart.getText() + rightPart.getText();
    }

    public void setText(String text) {
        leftPart.setText(text);
        rightPart.setText("");
    }

    public final void setTextColor(Color c) {
        leftPart.setColor(c);
        rightPart.setColor(c);
    }

    public final void setBackgroundColor( Color c )
    {
        background.setColor(c);
    }
    
    public final void setFont(Font font) {
        leftPart.setFont(font);
        rightPart.setFont(font);
    }

    public void addOnPressEnterListener(ActionListener listener) {
        onPressEnterListeners.addListener(listener);
    }

    public void removeOnPressEnterListener(ActionListener listener) {
        onPressEnterListeners.removeListener(listener);
    }

    @Override
    protected void onKeyPress(int key, char character) 
    {
        if ((int) character != 0) 
        {
            switch (key) {
                case Keyboard.KEY_BACK: {
                    // delete last character of left part 
                    String leftText = leftPart.getText();
                    final int leftLength = leftText.length();
                    if (leftText.length() > 0) {
                        leftPart.setText(leftText.substring(0, leftLength - 1));
                    }
                    break;
                }
                case Keyboard.KEY_DELETE: {
                    // delete first character of right part 
                    String rightText = rightPart.getText();
                    final int rightLength = rightText.length();
                    if (rightLength > 0) {
                        rightPart.setText(rightText.substring(1));
                    }
                    break;
                }
                case Keyboard.KEY_TAB:
                {
                    String newstring = leftPart.getText() + "    ";
                    leftPart.setText(newstring);
                    break;
                }
                case Keyboard.KEY_RETURN:
                    onPressEnterListeners.action();
                    break;
                default: {
                    // append character to left part
                    leftPart.setText(leftPart.getText() + character);
                    break;
                }
            }
        }
        else {
            // recalculate parts
            switch (key) {
                case Keyboard.KEY_LEFT: {
                    final String leftText = leftPart.getText();
                    final int leftLength = leftText.length();
                    if (leftLength > 0) {
                        final String rightText = rightPart.getText();
                        leftPart.setText(leftText.substring(0, leftLength - 1));
                        rightPart.setText(leftText.charAt(leftLength - 1)
                                          + rightText);
                    }
                    break;
                }
                case Keyboard.KEY_RIGHT: {
                    final String rightText = rightPart.getText();
                    final int rightLength = rightText.length();
                    if (rightLength > 0) {
                        final String leftText = leftPart.getText();
                        leftPart.setText(leftText + rightText.charAt(0));
                        rightPart.setText(rightText.substring(1));
                    }

                    break;
                }
                
            }
        }
        placeText();
    }

    private void placeText() {
        final int leftWidth = leftPart.getWidth();
        final int rightWidth = rightPart.getWidth();
        final int widgetWidth = getWidth();

        int leftOffset = leftPart.getPosX();
        int rightOffset = leftOffset + leftWidth;

        final int leftMargin = cursorMargin;
        final int rightMargin = widgetWidth - cursorMargin;
        if (rightOffset < leftMargin) {
            rightOffset = leftMargin;
            leftOffset = rightOffset - leftWidth;

        }
        else if (rightOffset > rightMargin) {
            rightOffset = rightMargin;
            leftOffset = rightOffset - leftWidth;
        }

        switch (align) {

            case LEFT: {
                if( leftOffset < leftMargin && 
                     rightOffset+rightWidth < rightMargin)
                {
                    rightOffset = rightMargin - rightWidth;
                    leftOffset = rightOffset - leftWidth;
                }
                
                if( leftOffset > leftMargin )
                {
                    leftOffset = leftMargin;
                    rightOffset = leftOffset + leftWidth;
                }
  
                break;
            }

            case RIGHT: {
                
                if( rightOffset + rightWidth > rightMargin &&
                    leftOffset > leftMargin)
                {
                    rightOffset = rightMargin -rightOffset;
                    leftOffset  = rightOffset - leftWidth;
                }
                
                if( rightOffset + rightWidth < rightMargin )
                {
                    rightOffset = rightMargin - rightWidth;
                    leftOffset  = rightOffset - leftWidth;
                }
                break;
            }

            case CENTER: {
                final int fullTextWidth = leftWidth + rightWidth;
                final int center = widgetWidth / 2;
                leftOffset = center - fullTextWidth / 2;
                rightOffset = leftOffset + leftWidth;
                break;
            }
        }

        final int textOffsetY = (getHeight() - leftPart.getHeight())/2;
        
        rightPart.setPosition(rightOffset, textOffsetY);
        leftPart.setPosition(leftOffset, textOffsetY);
        background.setPosition(0, 0);

        int cursorPos = rightOffset;
        cursor.setPosition(cursorPos, textOffsetY);
    }

    @Override
    protected void onDraw() throws Exception {
        if( inFocus )
        {
            long currentTime = Globals.Time.get() / 500;
            if( currentTime % 2 == 0)
            {
                cursor.setVisible(true);
            }
            else
            {
                cursor.setVisible(false);
            }
        }  
    }
 
    @Override
    protected void onGetFocus()
    {
        inFocus = true;
    }
    
    @Override
    protected void onLooseFocus()
    {
        inFocus = false;
        cursor.setVisible(false);
    }
    
    public void setAlign( HorizontalAlign align)
    {
        this.align = align;
        placeText();
    }

    @Override
    protected void onRefresh() {
        final int w = getWidth();
        final int h = getHeight();
        background.setSize(w, h);
        placeText();
    }
    
}

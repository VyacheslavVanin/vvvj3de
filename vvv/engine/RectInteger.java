/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vvv.engine;

/**
 *
 * @author vvv
 */
public final class RectInteger 
{
    private int bottom;
    private int left;
    private int height;
    private int width;
    
    public RectInteger(int left, int bottom, int width, int heigth)
    {
        set(left, bottom, width, heigth);
    }
    
    public  RectInteger()
    {
        this(0,0,0,0);
    }
    
    public void set(int left, int bottom, int width, int heigth)
    {
        this.setBottom(bottom);
        this.setLeft(left);
        this.setWidth(width);
        this.setHeight(heigth);
    }

    /**
     * @return the bottom
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * @param bottom the bottom to set
     */
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    /**
     * @return the left
     */
    public int getLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getTop()
    {
        return bottom + height;
    }
    
    public int getRight()
    {
        return left + width;
    }
 
    public static void intersection(RectInteger rect1, RectInteger rect2, RectInteger out)
    {
        final int bottom    = Math.max(rect1.getBottom(), rect2.getBottom());
        final int left      = Math.max(rect1.getLeft(), rect2.getLeft());
        
        final int top       = Math.min( rect1.getTop(), rect2.getTop() );
        final int right     = Math.min( rect1.getRight(), rect2.getRight() );
        
        final int width     = Math.max( right - left,   0 );
        final int height    = Math.max( top   - bottom, 0 );
       
        out.set( bottom, left, width, height );
    }
    
}

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
public final class Rect 
{
    private float bottom;
    private float left;
    private float height;
    private float width;
    
    public Rect(float left, float bottom, float width, float heigth)
    {
        set(left, bottom, width, heigth);
    }
    
    public  Rect()
    {
        this(0,0,0,0);
    }
    
    public void set(float left, float bottom, float width, float heigth)
    {
        this.setBottom(bottom);
        this.setLeft(left);
        this.setWidth(width);
        this.setHeight(heigth);
    }

    /**
     * @return the bottom
     */
    public float getBottom() {
        return bottom;
    }

    /**
     * @param bottom the bottom to set
     */
    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    /**
     * @return the left
     */
    public float getLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(float left) {
        this.left = left;
    }

    /**
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(float width) {
        this.width = width;
    }
    
    public float getTop()
    {
        return bottom + height;
    }
    
    public float getRight()
    {
        return left + width;
    }
 
    public static void intersection(Rect rect1, Rect rect2, Rect out)
    {
        final float bottom    = Math.max(rect1.getBottom(), rect2.getBottom());
        final float left      = Math.max(rect1.getLeft(), rect2.getLeft());
        
        final float top       = Math.min( rect1.getTop(), rect2.getTop() );
        final float right     = Math.min( rect1.getRight(), rect2.getRight() );
        
        final float width     = Math.max( right - left,   0 );
        final float height    = Math.max( top   - bottom, 0 );
       
        out.set( left, bottom, width, height );
    }
    
}

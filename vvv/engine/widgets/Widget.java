/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

/**
 *
 * @author Вячеслав
 */
public abstract class Widget 
{
    private float width;
    private float height;
    private float xpos;
    private float ypos;
    
    public float getWidth()  { return this.width; }
    public float getHeight() { return this.height;}
    public float getPosX()   { return this.xpos;  }
    public float getPosY()   { return this.ypos;  }
    
    public void  setWidth( float width)    { this.width = width; }
    public void  setHeight( float height ) { this.height = height; }
    public void  setPosX( float x)         { this.xpos = x; }
    public void  setPosY( float y)         { this.ypos = y; }
    
    public void draw() {}
    
    public abstract void onDraw();
    
    public void onMouseMove( float x, float y) {}
    
    public void onMouseButtonDown( float x, float y) {}
    
    public void onMouseButtonUp( float x, float y) {}
 
}

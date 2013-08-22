/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

/**
 *
 * @author vvv
 */
public class BaseButton extends Widget
{
    @Override
    public void onDraw() throws Exception 
    {       
    }
    
    @Override
    public boolean onMouseMove( float x, float y)
    { 
        return false;
    }
    
    @Override
    public boolean onLeftMouseButtonDown( float x, float y) 
    {
        return false;
    }
    
    @Override
    public boolean onLeftMouseButtonUp( float x, float y) 
    {
        return false;
    }
    
    
}

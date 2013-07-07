/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

/**
 *
 * @author Вячеслав
 */
public class StaticSprite extends Sprite
{
    private Texture texture = null;
    
    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }
    
    @Override
    public Texture getTexture(long millis)
    {
        return texture;
    }
    
}

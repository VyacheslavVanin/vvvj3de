/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.texture;

/**
 *
 * @author Вячеслав
 */


public class Texture
{
    private final TextureLowLevel texture;
    private final TexCoordData    coords;
    private final int             width;
    private final int             height;
    
    Texture(TextureLowLevel texture, TexCoordData coords, int widht, int height)
    {
        this.texture = texture;
        this.coords = coords;
        this.width = widht;
        this.height = height;
    }
  
    public TextureLowLevel getTexture()
    {
        return texture;
    }
    
    public TexCoordData getTexCoord()
    {
        return coords;
    }   
    
    public int getWidth() 
    {
        return this.width;
    }
    
    public int getHeight()
    {
        return this.height;
    }
}

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
    private TextureLowLevel texture;
    private TexCoordData    coords;
    private int             width;
    private int             height;
    
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

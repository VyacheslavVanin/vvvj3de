/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

/**
 *
 * @author Вячеслав
 */


public class Texture
{
    private TextureLowLevel texture;
    private TexCoordData    coords;
    
    Texture(TextureLowLevel texture, TexCoordData coords)
    {
        this.texture = texture;
        this.coords = coords;
    }

    TextureLowLevel getTexture()
    {
        return texture;
    }
    
    TexCoordData getTexCoord()
    {
        return coords;
    }   
}

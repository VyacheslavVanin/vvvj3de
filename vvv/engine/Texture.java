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
    
    
    TextureLowLevel getTexture()
    {
        return texture;
    }
    
    TexCoordData getTexCoord()
    {
        return coords;
    }   
}

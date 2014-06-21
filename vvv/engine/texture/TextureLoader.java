/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.texture;

import java.io.IOException;

/**
 *
 * @author Вячеслав
 */
public class TextureLoader
{
    /**\
     * Creates simple texture from file
     * @param filename
     * @param format
     * @return Texture
     * @throws IOException 
     */  
    static public Texture loadFromFile( String filename,
                                        TextureLowLevel.InternalFormat format )
            throws IOException
    {
        final Image im = ImageDesktop.read(filename, filename);
        final TextureLowLevel tll = new TextureLowLevel();
        tll.setFilter(TextureLowLevel.MINFILTER.LINEAR, 
                      TextureLowLevel.MAGFILTER.LINEAR);
        tll.loadToHost(im, format);
        final TexCoordData tcd = new TexCoordData();
        tcd.set(0, 0, 1, 1);
        final Texture t = new Texture( tll, tcd, 
                                 (int)im.getWidth(), (int)im.getHeight());
        return t;
    }
}

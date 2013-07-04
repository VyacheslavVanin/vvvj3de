/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Вячеслав
 */
public class TextureContainer
{
    public Texture GetTexture(String name)
    {
        return map.get(name);
    }
    
    public boolean addTexture(String fileName) throws IOException
    {
        return addTexture(fileName, fileName);
    }
    
    public boolean addTexture(String fileName, String name ) throws IOException
    {  
        if( input.containsKey(name))
        {
            return false;
        }
        Image im = ImageDesktop.read(fileName, name);
        input.put(name, im);
        
        return true;
    }
    
    /** equivalent to pack(0,GL_RGBA,false);*/
    public void pack()
    {
        pack(0, TextureLowLevel.InternalFormat.GL_RGBA, false);
    }
    
    public void pack(int border, TextureLowLevel.InternalFormat format, boolean useMipMap) 
    {
        List<Image>  in = new LinkedList<>( input.values() );
        List<Image>  out = new LinkedList<>();  
        while( !in.isEmpty() )
        {
            // create atlas
            TextureAtlas ta = new TextureAtlas(Constants.TEXTURE_MAX_SIZE, 
                                               Constants.TEXTURE_MAX_SIZE, 
                                               border, format);
            if( useMipMap )
            {
                ta.setFilters(TextureLowLevel.MINFILTER.LINEAR_MIPMAP_LINEAR, 
                              TextureLowLevel.MAGFILTER.LINEAR);
            }
            else
            {
                ta.setFilters(TextureLowLevel.MINFILTER.LINEAR, 
                              TextureLowLevel.MAGFILTER.LINEAR);
            }
            
            // pack to atlas
            ta.pack(in, out);
            List<Atlas.TextureData> l = ta.getList();
            for(Atlas.TextureData td: l)
            {             
                Texture t = new Texture( ta.getTexture(), td.getData(),
                                         (int)td.getImg().getWidth(),
                                         (int)td.getImg().getHeight());
                map.put(td.getName(), t);
            }
            
            // prepare for next iteration
            List<Image> tmp = in;
            in = out;
            out = tmp;
            out.clear();
        }
    }

    private HashMap<String,Texture>   map = new HashMap<>();// map for output and searching
    private HashMap<String,Image>   input = new HashMap<>();// list to accamulate input images before packing 
}

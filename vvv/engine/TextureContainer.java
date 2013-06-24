/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Вячеслав
 */
public class TextureContainer
{
    public Texture GetTexture(String name)
    {
        return null;
    }
    
    public void addTexture(String fileName)
    {   
    }
    
    public void addTexture(String fileName, String name )
    {  
    }
    
    public void addTexture(File file)
    {  
    }
    
    public void addTexture(File file, String name)
    {
    }
    
    public void pack() 
    {
    }
    
    private List<TextureAtlas> atlases = new ArrayList<>();
    private HashMap<String,Texture>  map = new HashMap<>();// map for output and searching
    private List<Image>        input = new ArrayList<>(); // list to accamulate input images before packing
    
}

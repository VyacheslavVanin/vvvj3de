/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Вячеслав
 */
public class TextureContainer_old
{
    public static final int MAX_TEXTURE_SIZE = 2048;
    
    private List<Atlas> atlases = new LinkedList<>();   
    private HashMap<String, Image> inputSet = new HashMap<>();

    public boolean addTexture(File file, String name) throws IOException
    {
        if( inputSet.containsKey( name ) )
        {
            return false;
        }
        
        BufferedImage img = ImageIO.read(file);
        if( img.getHeight() >  MAX_TEXTURE_SIZE
            || img.getWidth() > MAX_TEXTURE_SIZE )
        {
            return false;
        }
        
        inputSet.put( name,  new ImageFileData( img, name ) );
        return true;
    }

    public void addTexture(String fileName, String name) throws IOException
    {
        addTexture(new File(fileName), name);
    }

    public void addTexture(File file) throws IOException 
    {
        addTexture(file, file.getName());
    }

    public void addTexture(String fileName) throws IOException 
    {
        addTexture(new File(fileName));
    }

    public void pack( int border )
    {
        List<Image> input     = new LinkedList<>( inputSet.values() );
        List<Image> notPlaced = new LinkedList<>();
        List<Image> tmp; 
        
        while( !input.isEmpty() )
        {
            Atlas atlas =new Atlas(MAX_TEXTURE_SIZE, MAX_TEXTURE_SIZE, border);
            atlas.pack(input, notPlaced);
            atlases.add(atlas);
            tmp = input;
            input = notPlaced;
            notPlaced = tmp;
            notPlaced.clear();
        }     
    }

    public Texture getTexture(String name)
    {
        return null;
    }
    
    
    static private class ImageFileData implements vvv.engine.Image
    {
        private BufferedImage image;
        private String        name;
        
        public ImageFileData( BufferedImage image, String name)
        {
            this.image = image;
            this.name  = name;
        }
        
        @Override
        public float getHeight()
        {
            return image.getHeight();
        }

        @Override
        public float getWidth()
        {
            return image.getWidth();
        }

        @Override
        public String getName()
        {
            return name;
        }     

        @Override
        public ByteBuffer getByteBuffer()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getNumChanels()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}

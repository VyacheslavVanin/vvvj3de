/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vvv.engine.texture.Texture;
import vvv.engine.texture.TextureLoader;
import vvv.engine.texture.TextureLowLevel;
import vvv.engine.widgets.HorizontalAlign;
import vvv.engine.widgets.VerticalAlign;

/**
 *
 * @author Вячеслав
 */
public class Font
{
    private final Texture texture;

    private final Map<Character, GlyphInfo> map;
    private int ascenderHight;
    private int descenderHight; 
    
    /**
     * Load font information from "name" and "name.dsc"
     * @param name
     * @return 
     */
    static public Font loadFromFiles( String name ) throws IOException
    {
        Texture texture = TextureLoader.loadFromFile( 
                                        name, 
                                        TextureLowLevel.InternalFormat.GL_RGBA);
        
        Map<Character, GlyphInfo> map = getGlyphMapFromFile(name+".dsc");
        return  new Font(texture, map);
    }
    
  
    static private void readStringsFromFile( String name, List<String> list) throws IOException
    {
        Charset charset = Charset.forName("UTF-8");
        BufferedReader reader = Files.newBufferedReader( (new File(name)).toPath(), charset);
        String line;
        
        while( (line=reader.readLine())!= null )
        {
            String[] strings = line.split("\\s");
            for(int i=0; i < strings.length; ++i)
            {
                list.add(strings[i]);
            }
        }
    }
    
    static private GlyphInfo createWhiteSpaceGlyphInfo( String str)
    {
        GlyphInfo ret = new GlyphInfo();
        ret.width = Integer.parseInt( str );
        ret.symbol      = ' ';
        ret.offsetX     = 0;
        ret.offsetY     = 0;
        ret.glyphWidth  = 0;
        ret.glyphHeight = 0;
        ret.tx          = 0;
        ret.ty          = 0;
        ret.theight     = 0;
        ret.twidth      = 0;
        return ret;
    }
    
    static private GlyphInfo createGlyphInfo( List<String> l, int index)
    {
        int base = 1 + index*10;
        if( base > l.size()-1 )
        {
            return null;
        }
        GlyphInfo ret = new GlyphInfo();
        ret.symbol      = l.get( base++ ).charAt(0);
        ret.offsetX     = (int)Float.parseFloat( l.get( base++ ) );
        ret.offsetY     = (int)Float.parseFloat( l.get( base++ ) );
        ret.glyphWidth  = (int)Float.parseFloat( l.get( base++ ) );
        ret.glyphHeight = (int)Float.parseFloat( l.get( base++ ) );
        ret.width       = (int)Float.parseFloat( l.get( base++ ) );
        ret.tx          = Float.parseFloat( l.get( base++ ) );
        ret.ty          = Float.parseFloat( l.get( base++ ) );
        ret.theight     = Float.parseFloat( l.get( base++ ) );
        ret.twidth      = Float.parseFloat( l.get( base++ ) );
        return ret;
    }
    
    static private Map<Character, GlyphInfo> getGlyphMapFromFile(String name) throws IOException
    {
        List<String> l = new ArrayList<>();
        Map<Character, GlyphInfo> map = new HashMap<>();
        
        readStringsFromFile( name, l);
        
        GlyphInfo space = createWhiteSpaceGlyphInfo(l.get(0));
        map.put( ' ', space);
        
        int i = 0;
        GlyphInfo glyph;
        while( (glyph = createGlyphInfo(l, i++)) != null )
        {
            map.put( glyph.symbol, glyph);
        }
        
        return map;
    }
    
    private Font(Texture texture, Map<Character, GlyphInfo> map)
    {    
        this.map = map;
        this.texture = texture;
        this.ascenderHight = Integer.MIN_VALUE;
        this.descenderHight = Integer.MAX_VALUE;
        
        for( GlyphInfo gi: map.values() )
        {
            if( gi.offsetY > this.ascenderHight )
            {
                this.ascenderHight = gi.offsetY;
            }
            final int bottom = gi.offsetY - gi.glyphHeight;
            if( bottom < this.descenderHight )
            {
                this.descenderHight = bottom;
            }
        }       
    }
    
    public int getAscenderHight()
    {
        return ascenderHight;
    }
    
    public int getDescenderHight()
    {
        return descenderHight;
    }
 
    
    
    Texture getTexture() 
    {
        return texture;
    }
    
    GlyphInfo getGlyphInfo( char  ch) throws Exception
    {
        if( map.containsKey(ch) )
        {
            return map.get(ch);
        }
        throw new Exception("No glyph info of character " + ch);
    }
    
    static public class GlyphInfo
    {
        GlyphInfo()
        {
        }
        public int offsetX;
        public int offsetY;
        public int glyphWidth;
        public int glyphHeight;
        public int width;
        public float tx;
        public float ty;
        public float twidth;
        public float theight;
        public char  symbol;
    }   
}

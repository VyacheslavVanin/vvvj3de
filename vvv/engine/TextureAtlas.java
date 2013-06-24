/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

/**
 *
 * @author QwertyVVV
 */
public class TextureAtlas extends Atlas
{
    public TextureAtlas(float height, float width)
    {
        this(height,width,0); 
    }
    
    public TextureAtlas(float height, float width, float border)
    {
        this(height,width,border,TextureLowLevel.InternalFormat.GL_RGBA); 
    }
    
    public TextureAtlas( float height, float width, float border, 
                         TextureLowLevel.InternalFormat format )
    {
        super(height,width,border); 
        internalFormat = format;
        numChanels = internalFormatToNumChannels(format);
    }
    
    private static int internalFormatToNumChannels(TextureLowLevel.InternalFormat format)
    {
        int ret;
        switch(format)
        {
            case GL_DEPTH_COMPONENT:
            case GL_RED:   
                ret = 1;
                break;
            case GL_DEPTH_STENCIL:
            case GL_RG:
                ret = 2;
                break;
            case GL_RGB:
                ret = 3;
                break;
            case GL_RGBA:
                ret = 4;
                break;
            default:
                ret = 4;
                break;
        }
        return ret;
    }
    
    private void drawAllToImage()
    {
        int imType;
        switch(numChanels)
        {
            case 1:
                imType = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 2:
                imType = BufferedImage.TYPE_USHORT_GRAY;
                break;
            case 3:
                imType = BufferedImage.TYPE_3BYTE_BGR;
                break;
            case 4:
                imType = BufferedImage.TYPE_4BYTE_ABGR;
                break;
            default:
                imType = BufferedImage.TYPE_4BYTE_ABGR;
                break;
        }
        BufferedImage  im = new BufferedImage((int)getWidth(),
                                        (int)getHeight(),
                                        imType);
        
        Graphics g = im.getGraphics();
        
        float h =  getHeight();
        float w =  getWidth(); 
        int   b =  (int)getBorder();
        
        List<TextureData> list = getList();
        for( TextureData td: list)
        {
            java.awt.Image imageToDraw = ((ImageDesktop)td.getImg()).image;
            int x = (int)(td.getData().getX()*w);
            int y = (int)(td.getData().getY()*h);
            int imageH = imageToDraw.getHeight(null);
            int imageW = imageToDraw.getWidth(null);
            g.drawImage(imageToDraw, x, y, null); // draw main image
            
            if(b != 0)
            {
                // draw left margin
                g.drawImage(imageToDraw, x-b, y, x, y+imageH,
                                        0, 0, 1, imageH, null);
                // draw right margin
                g.drawImage(imageToDraw, x+imageW, y, x+imageW+b, y+imageH,
                                            imageW-1, 0, imageW, imageH, null);

                // draw top margin
                g.drawImage(imageToDraw, x, y, x+imageW, y-b,
                                        0, 0, imageW, 1, null);
                // draw bottom margin
                g.drawImage(imageToDraw, x, y+imageH, x+imageW, y+imageH+b,
                                         0, imageH-1, imageW, imageH, null);
                // draw top left square
                g.drawImage(imageToDraw, x, y, x-b, y-b,
                                         0, 0, 1, 1, null);
                // draw top right square
                g.drawImage(imageToDraw, x+imageW, y, x+imageW+b, y-b,
                                         imageW, 0, imageW-1, 1, null);
                // draw bottom left square
                g.drawImage(imageToDraw, x, y+imageH, x-b, y+imageH+b,
                                         0, imageH, 1, imageH-1, null);
                // draw bottom right square
                g.drawImage(imageToDraw, x+imageW, y+imageH, x+imageW+b, y+imageH+b,
                                         imageW, imageH, imageW-1, imageH-1, null);
            }
        }
        
        ByteBuffer bb = ImageDesktop.getByteBufferFromImage(im);
        
        texture = new TextureLowLevel();
        texture.loadToHost(bb, (int)w, (int)h, TextureLowLevel.numChannelsToImageFormat(numChanels), 
                                               internalFormat);
        texture.setFilter(minFilter,
                          magFilter);
//        try
//        {
//            ImageIO.write(im, "png", new File("out.png"));
//        }
//        catch (IOException ex)
//        {
//            Logger.getLogger(TextureAtlas.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    

    @Override
    public void pack(List<Image> in, List<Image> notPlaced)
    {
        super.pack(in, notPlaced);
        drawAllToImage();
    }
    
    public  TextureLowLevel getTexture()
    {
        return texture;
    }
    
    public void setFilters(TextureLowLevel.MINFILTER min, TextureLowLevel.MAGFILTER mag)
    {
        this.minFilter = min;
        this.magFilter = mag;
        texture.setFilter(minFilter,
                          magFilter);
    }
    
    private TextureLowLevel texture;
    
    private TextureLowLevel.InternalFormat  internalFormat = TextureLowLevel.InternalFormat.GL_RGBA;
    private TextureLowLevel.MINFILTER       minFilter = TextureLowLevel.MINFILTER.LINEAR;
    private TextureLowLevel.MAGFILTER       magFilter = TextureLowLevel.MAGFILTER.LINEAR;
    private int numChanels = 4;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sun.security.action.GetBooleanAction;

/**
 *
 * @author QwertyVVV
 */
public class TextureAtlas extends Atlas
{
    public TextureAtlas(float height, float width)
    {
        super(height,width);
    }
    
    public TextureAtlas(float height, float width, float border)
    {
        super(height,width,border); 
    }
    
    private void drawAllToImage()
    {
        BufferedImage  im = new BufferedImage((int)getWidth(),
                                        (int)getHeight(),
                                        BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics g = im.getGraphics();
        
        float h =  getHeight();
        float w =  getWidth(); 
        int   b =  (int)getBorder();
        
        List<textureData> list = getList();
        for( textureData td: list)
        {
            java.awt.Image imageToDraw = ((ImageData)td.getImg()).image;
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
        
        ByteBuffer bb = TextureLowLevel.getByteBufferFromImage(im);
        texture = new TextureLowLevel();
        texture.loadToHost(bb, (int)w, (int)h, TextureLowLevel.ImageFormat.GL_RGB, 
                                     TextureLowLevel.InternalFormat.GL_RGBA);
        
//        try
//        {
//            ImageIO.write(im, "png", new File("out.png"));
//        }
//        catch (IOException ex)
//        {
//            Logger.getLogger(TextureAtlas.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    static public class ImageData implements Image
    {
        private BufferedImage image;
        private String         name;
        
        public ImageData( BufferedImage image, String name )
        {
            this.image = image;
            this.name  =  name;
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

    }
    
    @Override
    public void pack(List<Image> in, List<Image> notPlaced)
    {
        super.pack(in, notPlaced);
        drawAllToImage();
    }
    
    private TextureLowLevel texture;
}

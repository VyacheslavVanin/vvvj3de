/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

/**
 *
 * @author Вячеслав
 */
public class ImageDesktop implements Image
{
    BufferedImage image;
    private String name;

    public ImageDesktop(BufferedImage image, String name)
    {
        this.image = image;
        this.name = name;
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
        return getByteBufferFromImage(image);
    }

    @Override
    public int getNumChanels()
    {
        return getNumComponentsInImage(image);
    }

    private static int getNumComponentsInImage(BufferedImage im)
    {
        return im.getData().getNumDataElements();
    }

    static ByteBuffer  getByteBufferFromImage(BufferedImage im)
    {
        /**
         * Platform dependent function
         */  
        return getinvertedY(im);
    }

    static private ByteBuffer getinvertedY(BufferedImage im)
    {
        int[] pdata;
        int iheight = im.getHeight();
        int iwidth = im.getWidth();
        int nn = getNumComponentsInImage(im);
        pdata = new int[im.getData().getHeight() * im.getData().getWidth() * nn];
        im.getData().getPixels(0, 0, iwidth, iheight, pdata);

        ByteBuffer bb = ByteBuffer.allocateDirect(nn * iheight * iwidth);
        for (int i = 0; i < pdata.length / nn; ++i)
        {
            int dx = i % iwidth;
            int dy = i / iwidth;
            int p = (iheight - dy - 1) * iwidth + dx;

            for (int j = 0; j < nn; ++j)
            {
                bb.put((byte) pdata[ p * nn + j]);
            }
        }
        bb.position(0);
        return bb;
    }
    
    static private ByteBuffer getNOTinvertedY(BufferedImage im)
    {
        int[] pdata;
        int iheight = im.getHeight();
        int iwidth = im.getWidth();
        int nn = getNumComponentsInImage(im);
        pdata = new int[im.getData().getHeight() * im.getData().getWidth() * nn];
        im.getData().getPixels(0, 0, iwidth, iheight, pdata);

        ByteBuffer bb = ByteBuffer.allocateDirect(nn * iheight * iwidth);
    
        for(int i = 0; i < pdata.length; ++i)
        {
            bb.put( (byte)pdata[i] );
        }
        bb.position(0);
        return bb;
    }
    
    static public ImageDesktop read(String fileName, String name) throws IOException
    {
        BufferedImage im = ImageIO.read( new File(fileName));
        return new ImageDesktop(im,name);
    }
}

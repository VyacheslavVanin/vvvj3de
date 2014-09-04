/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvvJ3dE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import javax.imageio.ImageIO;

/**
 *
 * @author Вячеслав
 */
public class testBufferedImage
{
    public static void main(String[] args) throws IOException
    {
        BufferedImage im = new BufferedImage(1024, 1024, BufferedImage.TYPE_USHORT_GRAY );
        Graphics    g = im.getGraphics();
            g.setColor(Color.red);
            BufferedImage im2 = ImageIO.read(new File("images/1.jpg"));
            HashSet<BufferedImage> hsi = new HashSet<>();
            hsi.add(im2);
            BufferedImage im3 = ImageIO.read(new File("images/1.jpg"));
            if( hsi.contains(im3) )
            {
                System.out.println("Aha!!!");
            }
            
            g.drawImage(im2, 0, 0, null);
        ImageIO.write(im, "png", new File("out.png")) ;
    }
}

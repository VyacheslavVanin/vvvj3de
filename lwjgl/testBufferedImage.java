/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
            g.drawImage(im2, 0, 0, null);
        ImageIO.write(im, "png", new File("out.png")) ;
    }
}

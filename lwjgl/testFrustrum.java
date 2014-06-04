/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;

/**
 *
 * @author Вячеслав
 */
public class testFrustrum
{
    static public void main(String[] args) throws IOException
    {
        VVVEngine l = new VVVEngine();
        try
        {
            l.setDisplayMode(1920, 1080, true);
            l.create();
            l.run();         
        }
        catch(LWJGLException ex)
        {
            Logger.getLogger(testFrustrum.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            l.destroy();
        }
    }
}

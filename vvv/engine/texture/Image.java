/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.texture;

import java.nio.ByteBuffer;

/**
 *
 * @author Вячеслав
 */
public interface Image 
{
    public ByteBuffer getByteBuffer();
    public int    getNumChanels();
    public float  getHeight();
    public float  getWidth(); 
    public String getName();

}

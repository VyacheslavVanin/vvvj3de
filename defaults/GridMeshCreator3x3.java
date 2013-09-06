/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package defaults;

import java.nio.ByteBuffer;

/**
 *
 * @author QwertyVVV
 */
public class GridMeshCreator3x3
{
    /**
     * Fill ibb with index information for a Mesh like this:<br>
     * 0--1--2--3<br>
     * |  |  |  |<br>
     * 4--5--6--7<br>
     * |  |  |  |<br>
     * 8--9--10-11<br>
     * |  |  |  |<br>
     * 12-13-14-15<br>
     * @param ibb Bytebuffer for storing indices
     *            Capacity must be at least 216 bytes
     */
    static void fillIndexBuffer(ByteBuffer ibb)
    {
        ibb.clear();
        for( int j = 0; j < 3; ++j)
        {
            for( int i = 0; i < 3; ++i)
            {
                int j4 = j*4;
                ibb.putInt(i + j4 + 0);
                ibb.putInt(i + j4 + 1);
                ibb.putInt(i + j4 + 5);
                ibb.putInt(i + j4 + 0);
                ibb.putInt(i + j4 + 5);
                ibb.putInt(i + j4 + 4);
            }
        }
        ibb.flip();
    }
    
    /**
     * Put one vertex to vbb 
     * @param vbb Buffer to store a vertex
     * @param x 
     * @param y
     * @param z
     * @param u
     * @param v 
     */
    static void putVertex(  ByteBuffer vbb, 
                             float x, float y, float z, 
                             float u, float v )
    {
        vbb.putFloat(x);
        vbb.putFloat(y);
        vbb.putFloat(z);
        vbb.putFloat(u);
        vbb.putFloat(v);
    }
    
    /**
     * @brief Fills vertex buffer with info for 3x3 Grid
     * @param vbb
     * @param width full width of grid
     * @param height full height of grid
     * @param topBorder height of top section of grid
     * @param bottomBorder height of bottom section of grid
     * @param leftBorder width of left section of grid
     * @param rightBorder  width of right section of grid
     */
    static void fillVertexBuffer( ByteBuffer vbb, float width, float height,
                                   float topBorder, float bottomBorder,
                                   float leftBorder, float rightBorder)
    {
        vbb.clear();
        float high = height;
        float low = 0;
        float highMidle = high - topBorder;
        float lowMidle  = low + bottomBorder;
        
        float left = 0; 
        float right = width;
        float leftMidle = left + leftBorder;
        float rightMidle  = right - rightBorder;
        
        putVertex( vbb, left,      high,    0,    0f, 1f);
        putVertex( vbb, leftMidle, high,    0, 0.25f, 1f);
        putVertex( vbb, rightMidle,high,    0, 0.75f, 1f);
        putVertex( vbb, right    , high,    0,    1f, 1f);
        
        putVertex( vbb, left,      highMidle,    0,    0f, 0.75f);
        putVertex( vbb, leftMidle, highMidle,    0, 0.25f, 0.75f);
        putVertex( vbb, rightMidle,highMidle,    0, 0.75f, 0.75f);
        putVertex( vbb, right    , highMidle,    0,    1f, 0.75f);
        
        putVertex( vbb, left,      lowMidle,    0,    0f, 0.25f);
        putVertex( vbb, leftMidle, lowMidle,    0, 0.25f, 0.25f);
        putVertex( vbb, rightMidle,lowMidle,    0, 0.75f, 0.25f);
        putVertex( vbb, right    , lowMidle,    0,    1f, 0.25f);
        
        putVertex( vbb, left,      low,    0,    0f, 0f );
        putVertex( vbb, leftMidle, low,    0, 0.25f, 0f );
        putVertex( vbb, rightMidle,low,    0, 0.75f, 0f );
        putVertex( vbb, right    , low,    0,    1f, 0f );
        vbb.flip();
    }
}

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
        final float high = height;
        final float low = 0;
        final float highMidle = high - topBorder;
        final float lowMidle  = low + bottomBorder;
        
        final float left = 0; 
        final float right = width;
        final float leftMidle = left + leftBorder;
        final float rightMidle  = right - rightBorder;
        
        final float baseTexOffset = 1.0f/16;
        final float lowTexMargin = 5 * baseTexOffset;
        final float highTexMargin = 1 - lowTexMargin;
        
        putVertex( vbb, left,      high,    0,    0f, 1f);
        putVertex( vbb, leftMidle, high,    0, lowTexMargin, 1f);
        putVertex( vbb, rightMidle,high,    0, highTexMargin, 1f);
        putVertex( vbb, right    , high,    0,    1f, 1f);
        
        putVertex( vbb, left,      highMidle,    0,    0f, highTexMargin);
        putVertex( vbb, leftMidle, highMidle,    0, lowTexMargin, highTexMargin);
        putVertex( vbb, rightMidle,highMidle,    0, highTexMargin, highTexMargin);
        putVertex( vbb, right    , highMidle,    0,    1f, highTexMargin);
        
        putVertex( vbb, left,      lowMidle,    0,    0f, lowTexMargin);
        putVertex( vbb, leftMidle, lowMidle,    0, lowTexMargin, lowTexMargin);
        putVertex( vbb, rightMidle,lowMidle,    0, highTexMargin, lowTexMargin);
        putVertex( vbb, right    , lowMidle,    0,    1f, lowTexMargin);
        
        putVertex( vbb, left,      low,    0,    0f, 0f );
        putVertex( vbb, leftMidle, low,    0, lowTexMargin, 0f );
        putVertex( vbb, rightMidle,low,    0, highTexMargin, 0f );
        putVertex( vbb, right    , low,    0,    1f, 0f );
        vbb.flip();
    }
}

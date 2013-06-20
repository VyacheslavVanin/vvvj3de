/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
/**
 *
 * @author Вячеслав
 */
public class Geometry
{
    private ByteBuffer vertices = null;
    private ByteBuffer indices  = null;
    private VertexAttribute[] vertexFormat = null;
    private int    indicesFormat = 0;
    private int    numIndices = 0;
    
    private int    ib = -1;
    private int    vb = -1;
    private int    vao = -1;
    
    private STATUS    status = STATUS.EMPTY;

    public static enum STATUS
    {
        EMPTY,
        ON_HOST,
        ON_DEVICE
    }

   
    public static class VertexAttribute
    {
       private int vertexAttr;
       private int numComponents;
       private int componentsType;
       private int stride;
       private int offset; 
      
       public VertexAttribute(int vertexAttribute, int numComponents, int componentsType)
       {
           this.vertexAttr = vertexAttribute;
           this.numComponents = numComponents;
           this.componentsType = componentsType;
           this.stride = 0;
           this.offset = 0;
       }

       public int getVertexAttribute(){ return vertexAttr;}
       public int getNumberOfComponents() { return numComponents;}
       public int getComponentsType() { return componentsType;}
       public int getStride() { return stride; }
       public int getOffset() { return offset; }

       void   setStride(int stride) { this.stride = stride;}
       void   setOffset(int offset) { this.offset = offset;}
       
    }
     
   
    public void loadToHost( ByteBuffer vertices,
                            VertexAttribute[] vertexformat,
                            ByteBuffer indices,  int numIndices,
                            int indicesFormat)
    {
        clearDevice();
        this.vertices = vertices;
        this.indices  = indices;
        this.vertexFormat = vertexformat;
        this.indicesFormat = indicesFormat;
        this.numIndices = numIndices;
        status = STATUS.ON_HOST;
    }
    
    private int sizeOfComponent(int glType)
    {
        switch( glType )
        {
            case GL_DOUBLE:
                     return 8;
            case GL_FLOAT: 
            case GL_INT:   
            case GL_UNSIGNED_INT: 
                     return 4;
            case GL_SHORT:
            case GL_UNSIGNED_SHORT:
                    return 2;
            case GL_BYTE:
            case GL_UNSIGNED_BYTE:
                     return 1;
            default:
                return -1;
        }
    }
    
    public void hostToDevice() 
    {
        clearDevice();
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        
        vb = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vb);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW );
        
        
        int stride = 0;
        for( VertexAttribute va: vertexFormat )
        {
            stride += va.numComponents * sizeOfComponent(va.componentsType);
        }
        
        int offset = 0; 
        for(int i=0; i< vertexFormat.length; ++i)
        {
            VertexAttribute va = vertexFormat[i];
            glVertexAttribPointer( va.getVertexAttribute(),
                                   va.getNumberOfComponents(), 
                                   va.getComponentsType(),
                                   false,
                                   stride, 
                                   offset);
            glEnableVertexAttribArray(va.getVertexAttribute());
            
            va.setOffset(offset);
            va.setStride(stride);
            offset += va.numComponents * sizeOfComponent( va.componentsType) ;
        }
        
        ib = glGenBuffers();
        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, ib);
        glBufferData( GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);    
        status = STATUS.ON_DEVICE;
    }
    
    public void clearDevice()
    {
        glDeleteBuffers(ib);
        glDeleteBuffers(vb);
        glDeleteVertexArrays(vao);
        ib = vb = vao = -1;
        status = STATUS.ON_HOST;
    }
    
    public void clearHost()
    {
        clearDevice();
        vertices        = null;
        indices         = null;
        vertexFormat    = null;
        status          = STATUS.EMPTY;
    }
    
    public void activate()
    {
        glBindVertexArray(vao);
    }
    
    public void draw()
    {
        if(status!= STATUS.ON_DEVICE)
        { 
            if( status == STATUS.ON_HOST)
            {
                hostToDevice();
            }
            
            if( status == STATUS.EMPTY)
            {
                return;
            }
        }
        glDrawElements(GL_TRIANGLES, numIndices, this.indicesFormat, 0);
    }
     
    public STATUS getStatus() 
    {
        return status;
    }    
}

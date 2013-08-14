/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
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
    private VertexAttribs vertexFormat = null;
    private int    indicesFormat = 0;
    private int    numIndices = 0;
    
    private int    ib = -1;
    private int    vb = -1;
    private int    vao = -1;
    
    static int     currentvao = -1;
    
    private STATUS    status = STATUS.EMPTY;

    public static enum STATUS
    {
        EMPTY,
        ON_HOST,
        ON_DEVICE
    }

   
    private static class VertexAttribPtr
    {
       private final int vertexAttr;
       private final int numComponents;
       private final int componentsType;
       private int stride;
       private int offset; 
      
       public VertexAttribPtr(int vertexAttribute, int numComponents, int componentsType)
       {
           this.vertexAttr = vertexAttribute;
           this.numComponents = numComponents;
           this.componentsType = componentsType;
           this.stride = 0;
           this.offset = 0;
       }

       int getVertexAttribute(){ return vertexAttr;}
       int getNumberOfComponents() { return numComponents;}
       int getComponentsType() { return componentsType;}
       int getStride() { return stride; }
       int getOffset() { return offset; }

       void   setStride(int stride) { this.stride = stride;}
       void   setOffset(int offset) { this.offset = offset;}
       
    }
     
    /**
     *  Contain Vertex Attributes
     */
    public static class VertexAttribs
    {
        private static final int ATTRIBUTE_VERTEX_POSITION = 0;
        private static final int ATTRIBUTE_VERTEX_NORMAL   = 1;
        private static final int ATTRIBUTE_VERTEX_TEXCOORD = 2;
        private static final int ATTRIBUTE_VERTEX_BINORMAL = 3;
        private static final int ATTRIBUTE_VERTEX_TANGENT  = 4;
        
        public static enum VERTEX_ATTRIBUTE
        {
            POSITION,
            NORMAL,
            TEXCOORD,
            BINORMAL,
            TANGENT
        };
        
        static public int vertexAttributeToInt( VERTEX_ATTRIBUTE va )
        {
            switch( va )
            {
                case POSITION:
                    return ATTRIBUTE_VERTEX_POSITION;
                case NORMAL:
                    return ATTRIBUTE_VERTEX_NORMAL;
                case TEXCOORD:
                    return ATTRIBUTE_VERTEX_TEXCOORD;
                case BINORMAL:
                    return ATTRIBUTE_VERTEX_BINORMAL;
                case TANGENT:
                    return ATTRIBUTE_VERTEX_TANGENT;
                default:
                   throw new IllegalArgumentException();
            }
        }
        
 
        private List<VertexAttribPtr> list = new ArrayList<>();
        private int stride = 0;
        
        /**
         * Add vertex attribute
         * @param vertexAttribute - attribute type (Position, Normal)
         * @param numComponents
         * @param glType 
         */
        public void add( VERTEX_ATTRIBUTE vertexAttribute, int numComponents, int glType )
        {
            VertexAttribPtr attrib = new VertexAttribPtr( vertexAttributeToInt(vertexAttribute), 
                                                         numComponents, 
                                                         glType);
            list.add(attrib);
            stride += numComponents * sizeOfComponent( glType );
        }
        
        public void enable()
        {  
            final int  listSize = list.size();
            for( int i=0; i < listSize; ++i)
            {
                VertexAttribPtr va = list.get(i);
            }

            int offset = 0; 
            for(int i=0; i< listSize; ++i)
            {
                VertexAttribPtr va = list.get(i);
                glVertexAttribPointer( va.getVertexAttribute(),
                                       va.getNumberOfComponents(), 
                                       va.getComponentsType(),
                                       false,
                                       stride, 
                                       offset );
                glEnableVertexAttribArray(va.getVertexAttribute());

                va.setOffset(offset);
                va.setStride(stride);
                offset += va.numComponents * sizeOfComponent( va.componentsType) ;
            }
        }
        
        public void disable()
        {
            final int  listSize = list.size();
            for(int i = 0;i < listSize; ++i)
            {
                VertexAttribPtr va = list.get(i);
                glDisableVertexAttribArray( va.getVertexAttribute() );
            }
        }
        
        public int getStride()
        {
            return stride;
        }
        
    }
   
    public void loadToHost( ByteBuffer vertices,
                            VertexAttribs vertexformat,
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
    
    private static int sizeOfComponent(int glType)
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
        
        vertexFormat.enable();
   
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
        if( currentvao != vao )
        {
            currentvao = vao;
            glBindVertexArray(vao);
        }
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

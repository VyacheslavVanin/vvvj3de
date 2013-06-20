package lwjgl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.util.glu.GLU.*;
import static vvv.engine.Consatants.*;
import vvv.engine.Geometry;
import vvv.engine.Geometry.VertexAttribute;
import vvv.engine.ShaderModel;
import vvv.engine.Texture;


/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class Lwjgl
{
    public static final int DISPLAY_HEIGHT = 480;
    public static final int DISPLAY_WIDTH  = 640;
    public static final Logger LOGGER = Logger.getLogger(Lwjgl.class.getName());
    private int squareSize;
    private int squareX;
    private int squareY;
    private int squareZ;

    private int vb;
    private int ib;
    private Geometry geom;
    private ShaderModel shm;
    private int vao;
    private int prog;
    private int tex;
    
    private static final int ATTRIB_LOCATION_POSITION = 0; 
    private static final int ATTRIB_LOCATION_TEXCOORD = 1;
    private static final int UNIFORM_LOCATION_TEXTURE0 = 0;
    
    
    static
    {
        try
        {
            LOGGER.addHandler(new FileHandler("errors.log", true));
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        }
    }

    public static void main(String[] args)
    {
        Lwjgl main = null;
        try
        {
            System.out.println( "Keys:" );
            System.out.println( "down  - Shrink" );
            System.out.println( "up    - Grow" );
            System.out.println( "left  - Rotate left" );
            System.out.println( "right - Rotate right" );
            System.out.println( "esc   - Exit" );
            main = new Lwjgl();
            main.create();
            main.run();
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
        finally
        {
            if (main != null)
            {
                main.destroy();
            }
        }
    }

    public Lwjgl()
    {
        squareSize = 100;
        squareX = 0;
        squareY = 0;
        squareZ = 0;
    }

    public void create() throws LWJGLException
    {
        //Display
        Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        Display.setFullscreen(false);
        Display.setTitle("Hello LWJGL World!");   
        Display.create();

        //Keyboard
        Keyboard.create();

        //Mouse
        Mouse.setGrabbed(false);
        Mouse.create();

        //OpenGL
        initGL();
        resizeGL();
    }

    public void destroy()
    {
        //Methods already check if created before destroying.
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public int vvvShaderStatus(int shader, int param)
    {
        int result = glGetShaderi(shader, param);
        if( result != GL_TRUE)
        {
            String str = glGetShaderInfoLog(shader, result);
            System.out.println("Shader Error: " + str);
        }
        return result;
    }
    
    public int vvvProgramStatus(int shader, int param)
    {
        int result = glGetProgrami(param, param);
        if( result != GL_TRUE)
        {
            String str = glGetProgramInfoLog(shader, result);
            System.out.println("Program Error: " + str);
        }
        return result;
    }
    
    public void vvvInitShader()
    {
        
        String vshSource = "#version 330 core\n"
        //    + "uniform mat4 projectionMatrix;\n"
            + "layout(location = 0) in vec3 position;\n"
            + "layout(location = 2) in vec2 texCoord;\n"
      //      + "in vec3 color;\n"
            + "out vec2 textureCoordinates;\n"
            + "void main(void)\n"
            + "{\n"
             // перевод вершинных координат в однородные
            +"       gl_Position   = vec4(position, 1.0); \n"
            +"       textureCoordinates = texCoord;\n"
             // передаем цвет вершины в фрагментный шейдер
       //     "      fragmentColor = color;\n"
            + "}\n";
        
        String fshSource = "#version 330 core\n"
       //     + "in vec3 fragmentColor;"
            + "uniform sampler2D Texture0;\n"
            + "in vec2 textureCoordinates;\n"
            + "out vec4 color;\n"
            + "void main(void)\n"
            + "{\n"
          //  + "    color = vec4( 1.0, 1.0, 0, 1.0);\n" // зададим цвет пикселя
            + "    color = texture(Texture0,textureCoordinates);\n" // зададим цвет пикселя
         //   +"     if( color.b > 0.90) discard;\n"
            + "}\n";
        

        shm = new ShaderModel();
        shm.load(vshSource, fshSource);
        
    }
    
    public void vvvInitGeometry()
    {
        float[] vertices = { -0.5f, -0.5f, 1,  0,0, 
                             -0.5f,  0.5f, 1,  0,1,
                              0.5f,  0.5f, 1,  1,1,
                              0.5f,  -0.5f, 1, 1,0};
        int[] indices  = { 0, 1, 3, 
                             1, 2, 3};
        
   
        ByteBuffer  vbb = BufferUtils.createByteBuffer( vertices.length * 4);
        for(int i =0; i < vertices.length; ++i)
        {
            vbb.putFloat( vertices[i] );
        }
        vbb.flip();
        
        ByteBuffer ibb = BufferUtils.createByteBuffer( indices.length * 4);
        for( int i =0; i < indices.length; ++i)
        {
            ibb.putInt( indices[i] );
        }
        ibb.flip();
        
        VertexAttribute[] attribs 
            =   { new VertexAttribute(ATTRIBUTE_VERTEX_POSITION,
                                            3, GL_FLOAT),
                  new VertexAttribute(ATTRIBUTE_VERTEX_TEXCOORD, 
                                            2, GL_FLOAT)
                };
        geom = new Geometry();
        geom.loadToHost( vbb, attribs, ibb, indices.length, GL_UNSIGNED_INT);
    }
    
    public void vvvInitTexture()
    {
        tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);

        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                        GL_LINEAR_MIPMAP_LINEAR );
        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
                        GL_LINEAR_MIPMAP_LINEAR );

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        int[] pdata ;
        try
        {
             BufferedImage im = ImageIO.read( new File("texture.png") );
             int iheight = im.getHeight();
             int iwidth  = im.getWidth();
             int nn = im.getData().getNumDataElements();
             pdata  = new int[im.getData().getHeight() * im.getData().getWidth()* nn ]; 
             im.getData().getPixels(0, 0, iwidth, iheight, pdata);
             
             ByteBuffer bb = ByteBuffer.allocateDirect(  nn * iheight * iwidth);
             for( int i=0 ; i < pdata.length / nn; ++i )
             {
                 int dx = i % iwidth;
                 int dy = i / iwidth;
                 int p = (iheight - dy-1) * iwidth + dx;
                 if( p >= pdata.length || p < 0 )
                 {
                     System.out.println( "Aha!!!" );
                 }
                 
                 for( int j = 0; j < nn; ++j)
                 {
                    bb.put(  (byte)pdata[ p*nn + j ] );
                 }
             }
             bb.position(0);
            
           //  gluBuild2DMipmaps(GL_TEXTURE_2D, nn, iwidth, iheight, GL_RGB, GL_UNSIGNED_BYTE, bb);
             
             int imageFormat;
            switch(nn)
            {
                case 3: imageFormat = GL_RGB;  break;
                case 4: imageFormat = GL_RGBA; break;
                default:
                    return;
            }
             
             glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, iwidth, iheight, 0, 
		                         imageFormat, GL_UNSIGNED_BYTE, bb);
             glGenerateMipmap(GL_TEXTURE_2D);
             
        }
        catch (IOException ex)
        {
            Logger.getLogger(Lwjgl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void vvvInit()
    {  
        vvvInitGeometry();
        vvvInitShader(); 
        vvvInitTexture();
    }
    
    public void initGL()
    {
        //2D Initialization
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        vvvInit();     
    }

    public void processKeyboard()
    {
        //Square's Size
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
            --squareSize;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
        {
            ++squareSize;
        }

        //Square's Z
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
            ++squareZ;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
            --squareZ;
        }
    }

    public void processMouse()
    {
        squareX = Mouse.getX();
        squareY = Mouse.getY();
    }

    public void vvvRender()
    {  
        shm.activate();
        
        glBindTexture(GL_TEXTURE_2D, tex);
        int loc = glGetUniformLocation(prog, "Texture0");
        glUniform1i( loc, 0);
      
        
        geom.activate();
        geom.draw();      
    }
    
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT);
        vvvRender();
        
    }

    public void resizeGL()
    {
        //2D Scene
        glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f, DISPLAY_WIDTH, 0.0f, DISPLAY_HEIGHT);
        glPushMatrix();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glPushMatrix();
    }

    public void run()
    {
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(
            Keyboard.KEY_ESCAPE))
        {
            if (Display.isVisible())
            {
                processKeyboard();
                processMouse();
                update();
                render();
            }
            else
            {
                if (Display.isDirty())
                {
                    render();
                }
                
//                try
//                {
//                    Thread.sleep(100);
//                }
//                catch (InterruptedException ex)
//                {
//                }
            }
            Display.update();
            Display.sync(60);
        }
    }

    public void update()
    {
        if (squareSize < 5)
        {
            squareSize = 5;
        }
        else if (squareSize >= DISPLAY_HEIGHT)
        {
            squareSize = DISPLAY_HEIGHT;
        }
    }
}

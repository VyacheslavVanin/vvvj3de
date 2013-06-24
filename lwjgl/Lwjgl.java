package lwjgl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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
import vvv.engine.*;
import static vvv.engine.Constants.*;
import vvv.engine.Geometry.VertexAttribute;
import vvv.engine.TextureLowLevel.TextureNotLoadedException;


/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class Lwjgl
{
    public static final int DISPLAY_HEIGHT = 800;
    public static final int DISPLAY_WIDTH  = 800;
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
   // private TextureLowLevel tll;
    private Texture tll;
    private Texture[] texlist = new Texture[10];
    private TextureContainer tcontainer;
    
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

    private static void vvvtest() throws IOException
    {
        TextureAtlas ta = new TextureAtlas(2048, 2048,1);
        List<Image> atlasInputImages = new ArrayList<>();
        List<Image> notPlaced = new ArrayList<>();
        
        for(int i = 0; i < 10; ++i)
        {
           // String fileName = "images/generated"+i+".png";
            String fileName = "images/"+i+".jpg";
            BufferedImage bi = ImageIO.read( new File(fileName) );
         //   TextureAtlas.ImageData  id = 
          //                           new TextureAtlas.ImageData(bi, fileName);
            
         //   atlasInputImages.add(id);
        }
        
        ta.pack(atlasInputImages, notPlaced);
      //  ta.drawAllToImage();
        
    }
    
    public static void main(String[] args)
    {
        Lwjgl main = null;
        
        try
        {
           // vvvtest();
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
        catch (LWJGLException /*| IOException*/ ex)
        {
            Logger.getLogger(Lwjgl.class.getName()).log(Level.SEVERE, null, ex);
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
            + "uniform  vec4 TexCoordData; \n"
            + "void main(void)\n"
            + "{\n"
             // перевод вершинных координат в однородные
            +"       gl_Position   = vec4(position, 1.0); \n"
           // +"       textureCoordinates   = texCoord; \n"
           // +"       textureCoordinates.x = TexCoordData.x + texCoord.x*TexCoordData.z;\n"
           // +"       textureCoordinates.y = TexCoordData.y + texCoord.y*TexCoordData.w;\n"
            +"       textureCoordinates   = TexCoordData.xy + texCoord*TexCoordData.zw; \n"
           // +"       textureCoordinates.x =  texCoord.x;\n"
           // +"       textureCoordinates.y =  texCoord.y*TexCoordData.w;\n"
           // +"       textureCoordinates   =  texCoord*TexCoordData.z; \n"
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
    
    public void vvvInitTexture() throws IOException
    {
        tcontainer = new TextureContainer();
       
        for(int i = 0; i < 10; ++i)
        {
            tcontainer.addTexture("images/"+i+".jpg");
        }
       
        tcontainer.pack(1, TextureLowLevel.InternalFormat.GL_RGBA, true);
        
        for(int i=0; i < 10; ++i)
        {
            texlist[i] = tcontainer.GetTexture("images/"+i+".jpg");
        }
        tll = texlist[0];
    }
    
    public void vvvInit() throws IOException
    {  
        vvvInitGeometry();
        vvvInitShader(); 
        vvvInitTexture();
    }
    
    public void initGL()
    {
        //2D Initialization
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        try
        {
            vvvInit();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Lwjgl.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    private int currentImage = 0;
    public void processKeyboard()
    {
        //Square's Size
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
            --squareSize;
            tll = texlist[currentImage++ % 10];
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

    public void vvvRender() throws TextureNotLoadedException
    {  
        shm.activate();       
        shm.setTexture(0, tll);
        
        geom.activate();
        geom.draw();      
    }
    
    public void render() throws TextureNotLoadedException
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
        while (!Display.isCloseRequested() && 
               !Keyboard.isKeyDown( Keyboard.KEY_ESCAPE))
        {
            try
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
            }
            }
            catch(TextureNotLoadedException ex)
            {
                LOGGER.log(Level.SEVERE,ex.toString(),ex);
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

package lwjgl;

import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import vvv.engine.*;
import vvv.engine.TextureLowLevel.TextureNotLoadedException;


/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class Lwjgl
{
    public static final int DISPLAY_HEIGHT = 600;
    public static final int DISPLAY_WIDTH  = 600;
    public static final Logger LOGGER = Logger.getLogger(Lwjgl.class.getName());
    private int squareSize;
    private int squareZ;

    private ShaderModel shm;
    private Texture tll;
    private Texture[] texlist = new Texture[10];
    private TextureContainer tcontainer;
   
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
        shm = new ShaderModel();
        try
        {
            shm.loadFromFiles("shaders/sprite.vs", "shaders/sprite.fs");
        }
        catch (IOException ex)
        {
            Logger.getLogger(Lwjgl.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        vvvInitTexture();
        vvvInitShader();
        
        screen = new Screen();
        SpriteLayer sl = new SpriteLayer();
            
        screen.addLayer(sl);
        sl.setShader(shm);
        
        Random r = new Random();
        
        for(int i=0; i < 20000; ++i)
        {
            Sprite     spr = new Sprite();
            
            spr.setTexture( texlist[i%10] );
            spr.setScale(16, 16, 1);
            spr.setPosition( (r.nextInt()%DISPLAY_WIDTH/2), 
                             (r.nextInt()%DISPLAY_HEIGHT/2),
                             0);
            
            sl.addObject(spr);
            sprite1 = spr;
        }
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
            sprite1.move(0, -1, 0);
            
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
        {
            ++squareSize;
            sprite1.move(0, 1, 0);
        }

        //Square's Z
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
            ++squareZ;
            sprite1.move(-1, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
            --squareZ;
            sprite1.move( 1, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            --squareZ;
            tll = texlist[currentImage++ % 10];
            sprite1.setTexture(tll);    
        }
    }

    public void processMouse()
    {
        int squareX = Mouse.getX();
        int squareY = Mouse.getY();
        
       // sprite1.setPosition( squareX-DISPLAY_WIDTH/2, squareY-DISPLAY_HEIGHT/2, 0 );
    }

    
    public void render() throws TextureNotLoadedException
    {
        glClear(GL_COLOR_BUFFER_BIT);
        screen.draw();
    }

    public void resizeGL()
    {
        //2D Scene
        screen.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
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
           // Display.sync(60);
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
    
    public Screen screen;
    public Sprite sprite1;
}

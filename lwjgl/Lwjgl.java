package lwjgl;

import vvv.engine.layers.SpriteLayer;
import vvv.engine.layers.Screen;
import vvv.engine.shader.ShaderModel;
import vvv.engine.texture.TextureContainer;
import vvv.engine.texture.Texture;
import vvv.engine.texture.TextureLowLevel;
import vvv.engine.sprite.SpriteAnimation;
import vvv.engine.sprite.StaticSprite;
import vvv.engine.sprite.AnimatedSprite;
import java.io.IOException;
import java.util.ArrayList;
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
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import vvv.engine.*;
import vvv.engine.texture.TextureLowLevel.TextureNotLoadedException;
import vvv.math.FloatMath;


/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class Lwjgl
{
    public static final int DISPLAY_HEIGHT = 800;
    public static final int DISPLAY_WIDTH  = 1280;
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
        DisplayMode displayMode = new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        DisplayMode[] modes = Display.getAvailableDisplayModes();

         for (int i = 0; i < modes.length; i++)
         {
             if (modes[i].getWidth() == DISPLAY_WIDTH
             && modes[i].getHeight() == DISPLAY_HEIGHT
             && modes[i].isFullscreenCapable())
               {
                    displayMode = modes[i];
               }
         }
         
        Display.setDisplayMode(displayMode);
        Display.setVSyncEnabled(true);
        //Display.setFullscreen(true);
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
        
        for( int i = 0; i < 3; ++i)
        {
            tcontainer.addTexture("images/v" + (i+1) + ".png");
        }
        
        for( int i = 0; i < 10; ++i)
        {
            tcontainer.addTexture("images/rendered/idle/000" + i + ".png");
            tcontainer.addTexture("images/rendered/rotation/000" + i + ".png");
        }
        
        for( int i = 10; i < 30; ++i)
        {
            tcontainer.addTexture("images/rendered/idle/00" + i + ".png");
            tcontainer.addTexture("images/rendered/rotation/00" + i + ".png");
        }
        
        
        tcontainer.pack(1, TextureLowLevel.InternalFormat.GL_RGBA, true);
        
        
        texlist = new Texture[13];
        for(int i=0; i < 10; ++i)
        {
            texlist[i] = tcontainer.GetTexture("images/"+i+".jpg");
        }
      
        texlist[10] = tcontainer.GetTexture("images/v1.png");
        texlist[11] = tcontainer.GetTexture("images/v2.png");
        texlist[12] = tcontainer.GetTexture("images/v3.png");
        tll = texlist[0];
    }
    
    public void vvvInitAnimation()
    {   
        
        ArrayList<Texture> frames = new ArrayList<>();
        for(int i = 0; i < 10; ++i)
        {
            frames.add( tcontainer.GetTexture("images/rendered/idle/000" + i + ".png"));
        }
        for( int i = 10; i < 30; ++i)
        {
            frames.add( tcontainer.GetTexture("images/rendered/idle/00" + i + ".png"));
        } 
        animationIdle = new SpriteAnimation(frames, 1200 );
        
        
        ArrayList<Texture> framesRot = new ArrayList<>();
        for(int i = 0; i < 10; ++i)
        {
            framesRot.add( tcontainer.GetTexture("images/rendered/rotation/000" + i + ".png"));
        }
        for( int i = 10; i < 30; ++i)
        {
            framesRot.add( tcontainer.GetTexture("images/rendered/rotation/00" + i + ".png"));
        }
        animationRotation = new SpriteAnimation(framesRot, 1200);
    }
    
    public void vvvInit() throws IOException
    {  
        vvvInitTexture();
        vvvInitAnimation();
        vvvInitShader();
        
        screen = new Screen();
        SpriteLayer sl = new SpriteLayer();
            
        screen.addLayer(sl);
        sl.setShader(shm);
        
        Random r = new Random();
        
        for(int i=0; i < 20000; ++i)
        {
            StaticSprite     spr = new StaticSprite();
            tll = texlist[i%10];
            spr.setTexture( tll );
            spr.setScale(tll.getWidth(), tll.getHeight(), 1);
            spr.setPosition( (r.nextInt()%DISPLAY_WIDTH*20), 
                             (r.nextInt()%DISPLAY_HEIGHT*20),
                             0);
            
            sl.addObject(spr);
           // sprite1 = spr;
        }
        sprite1 = new AnimatedSprite();
        
        
        sprite1.setPosition( 0, 0, 0);
        sprite1.setScale( 128, 128, 1);
        sprite1.setAnimation(animationIdle);
        sprite1.playAnimation();
        
        StaticSprite spr = new StaticSprite();
        tll = texlist[11];
        spr.setTexture( tll );
        spr.setPosition( 32, 32, 0);
        spr.setScale( tll.getWidth(), tll.getHeight(), 1);
        
        sl.addObject(spr);
        sl.addObject(sprite1);
        camera = sl.getCamera();
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
    private float velocity = 250;
    public void processKeyboard()
    {   
        boolean pressed = false;
        //Square's Size
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
            --squareSize;
            sprite1.move(0, -1, 0);
           // camera.move(0, -1, 0, 1);
            camera.moveDown(1.0f);
            
            pressed = true;
        }
                
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
        {
            ++squareSize;
            sprite1.move(0, 1, 0);
            //camera.move(0, 1, 0, 1);
            camera.moveUp(1.0f);
            pressed = true;
        }

        //Square's Z
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
            ++squareZ;
            sprite1.move(-1, 0, 0);
            camera.moveLeft(1.0f);
            pressed = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
            --squareZ;
            sprite1.move( 1, 0, 0);
            camera.moveRight(1.0f);
            pressed = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            --squareZ;
            sprite1.setAnimationSpeed(4);
            velocity = 1000;
           // tll = texlist[currentImage++ % 10];
           // sprite1.setTexture(tll);   
           // sprite1.setScale(tll.getWidth(), tll.getHeight(), 1);
        }
        else
        {
            sprite1.setAnimationSpeed(1);
            velocity = 500;
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_Z))
        {
            sprite1.rotate(  0.01f, 0.0f,0.0f,1.0f);    
        }
        
        if( Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            Matrix4f matrix4f = sprite1.getMatrix4f();
            System.out.println(matrix4f);
        }
        
//        if( pressed == false)
//        {
//            sprite1.setAnimation(animationIdle);
//        }
//        else
//        {
//            sprite1.setAnimation(animationRotation);
//        }
    }

    
    Vector2f  tvup = new Vector2f(0, 1);
    Vector2f  mouse = new Vector2f();
    Vector2f  direction = new Vector2f(0,1);
    public void processMouse()
    {
        mouse.x = Mouse.getX() - DISPLAY_WIDTH/2  ;
        mouse.y = Mouse.getY() - DISPLAY_HEIGHT/2 ;
        if( mouse.lengthSquared() > 0)
        {
            mouse.normalise();
        }
        else
        {
            mouse.x = 1;
            mouse.y = 0;
        }
        
        float a = Vector2f.angle(tvup, mouse);
        if( mouse.x > 0)
        {
            a *= -1;
        }
        sprite1.setRotation(a, 0, 0, 1);
        direction.x = -FloatMath.sin(a);
        direction.y = FloatMath.cos(a);
        
        if( Mouse.isButtonDown(0))
        {
            //final float velocity = 500.0f;
            float dtf = 0;
            long dt = 0;
            long  t = System.currentTimeMillis();
            if(MB0Pressed == false)
            {                            
            }
            else
            {       
                dt =  t - timeLastPressed; 
            }
            dtf = dt / 1000.0f;
            timeLastPressed = t;
            
            MB0Pressed = true;
            
            float s = dtf * velocity;
            sprite1.move( direction.x * s, direction.y * s, 0);
            camera.move(direction.x * s, direction.y * s, 0);
            sprite1.setAnimation(animationRotation);
        }
        else
        {
           MB0Pressed = false;
           sprite1.setAnimation(animationIdle); 
        }
       // sprite1.setPosition( squareX-DISPLAY_WIDTH/2, squareY-DISPLAY_HEIGHT/2, 0 );
    }
    private boolean MB0Pressed = false;
    private long    timeLastPressed = 0;
    
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
    
    public Screen screen;
    public AnimatedSprite sprite1;
    public Camera camera;
    public SpriteAnimation animationIdle;
    public SpriteAnimation animationRotation;
}

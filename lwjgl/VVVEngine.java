package lwjgl;

import defaults.DefaultButton;
import defaults.DefaultCheckbox;
import defaults.DefaultHorizontalSlider;
import defaults.DefaultPanel;
import defaults.DefaultSystemColors;
import defaults.DefaultWidgetLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import vvv.engine.Camera;
import vvv.engine.VariableColor;
import vvv.engine.sprite.AnimatedSprite;
import vvv.engine.sprite.SpriteAnimation;
import vvv.engine.sprite.StaticSprite;
import vvv.engine.texture.Texture;
import vvv.engine.texture.TextureContainer;
import vvv.engine.texture.TextureLowLevel;
import vvv.engine.texture.TextureLowLevel.TextureNotLoadedException;
import vvv.engine.widgets.*;
import vvv.math.FloatMath;


/**
 * @author jediTofu
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class VVVEngine
{
    private  int DISPLAY_HEIGHT = 600;
    private  int DISPLAY_WIDTH  = 800;
    private  boolean fullScreen = false;
    
    public static final Logger LOGGER = Logger.getLogger(VVVEngine.class.getName());
 
    private Texture tll;
    private Texture[] texlist = new Texture[10];
    private TextureContainer tcontainer = null;
   
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
        VVVEngine main = null;
        
        try
        {
            main = new VVVEngine();
            main.create();
            main.run();
        }
        catch (LWJGLException /*| IOException*/ ex)
        {
            Logger.getLogger(VVVEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            if (main != null)
            {
                main.destroy();
            }
        }
    }


    public void setDisplayMode(int width, int height, boolean fullScreen )
    {
        DISPLAY_HEIGHT = height;
        DISPLAY_WIDTH = width;
        this.fullScreen = fullScreen;
    }
    
    public void create() throws LWJGLException
    {
        //Display
        DisplayMode displayMode = new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        DisplayMode[] modes = Display.getAvailableDisplayModes();

        if( fullScreen)
        {
            for (DisplayMode mode : modes) 
            {
                if (mode.getWidth() == DISPLAY_WIDTH && mode.getHeight() == DISPLAY_HEIGHT && mode.isFullscreenCapable()) 
                {
                    displayMode = mode;
                    Display.setFullscreen(true );
                }
            }
        }
        
        Display.setDisplayMode(displayMode);
        Display.setVSyncEnabled(true);
        //
        Display.setTitle("Hello LWJGL World!");   
        Display.create( new PixelFormat(), new ContextAttribs(3, 3));
        
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
        
        tcontainer.addTexture("images/gui/test.png");
        tcontainer.addTexture("images/gui/test2.png");
        
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
    
    static Random random = new Random();
    
    public void vvvInitGui(Screen screen) throws IOException
    {
        WidgetLayer wl = new DefaultWidgetLayer();
        
        
        final ActionListener listener = new ActionListener() {
            @Override
            public void action() {
                StaticSprite     spr = new StaticSprite();
                tll = texlist[11];
                spr.setTexture( tll );
                spr.setScale( tll.getWidth(), tll.getHeight(), 1 );
                spr.setPosition( sprite1.getPosition().getX(), 
                                 sprite1.getPosition().getY(),
                                 sprite1.getPosition().getZ()+1);
                bgSpriteLayer.addObject(spr);
            }
        };
        
        final ActionListener changeColor = new ActionListener()
        {
            @Override
            public void action() 
            {
                VariableColor c = DefaultSystemColors.getControlColor();
                c.setColor( random.nextFloat(), random.nextFloat(), random.nextFloat());
            }
        };
        
        DefaultPanel panel3 = new DefaultPanel(500,350);
            panel3.setPosition(50, 300);
            Layout vlayout = new VerticalLayout();
                for( int i = 0; i < 5; ++i)
                {
                    DefaultButton bb = new DefaultButton("Button " + i);
                    bb.setAutoSize(false);
                    bb.setText("Very very Long String 1234567890!");
                    bb.setSize( 200, bb.getHeight()); 
                    bb.addOnClickListener(listener);
                    vlayout.addWidget(bb);
                }
                
                DefaultCheckbox check = new DefaultCheckbox("My Checkbox");
                final ActionListener onCheckListener = new ActionListener() 
                {
                    @Override
                    public void action() 
                    {
                        TestDisable.setEnabled(false);
                    }
                };
                
                final ActionListener onUncheckListener = new ActionListener() 
                {
                    @Override
                    public void action() 
                    {
                        TestDisable.setEnabled(true);
                    }
                };
                
                check.addOnCheckListener(onCheckListener);
                check.addOnUncheckListener(onUncheckListener);
               
                vlayout.addWidget(check);
               
                
                
                ActionListener sliderListener = new ActionListener() 
                {
                    @Override
                    public void action() 
                    {
                        tl.setText( "" + slider.getValue() );
                        //VariableColor c = DefaultSystemColors.getPanelColor();
                        //c.setAlpha( slider.getValue() / (float)slider.getRange() );
                        //panel.setWidth( 200 + slider.getValue()/(float)slider.getRange() * 200 );
                        panel.setSize( 300 + slider.getValue()/(float)slider.getRange() * 200, panel.getHeight());
                    }
                };
                slider = new DefaultHorizontalSlider();
                slider.setRange(50);
                slider.addOnDragListener(sliderListener);
                vlayout.addWidget(slider);
                
                
                
            panel3.addWidget(vlayout);
        wl.addObject(panel3);       
  
        DefaultPanel panel4 = new DefaultPanel(500,50);
            panel4.setColor( 1, 1, 1, 1.5f);
            panel4.setPosition(50, 240);
            Layout hlayout = new HorizontalLayout();
                for( int i = 0; i < 4; ++i)
                {
                    DefaultButton bb = new DefaultButton("Button " + i);
                    bb.addOnClickListener( changeColor);
                    hlayout.addWidget( bb );
                }
               TestDisable = panel4;
            panel4.addWidget(hlayout);
        panel4.addWidget(hlayout);    
        wl.addObject(panel4);
        panel = panel4;
        
                
        tl = new TextLabel( "text" );
        tl.setColor(1, 1, 1, 1);
        tl.setPosition(500, 100);
        wl.addObject(tl);
              
        screen.setGuiLayer(wl); 
    }
    Panel     panel = null;
    TextLabel activeLabel = null;
    int       clickcounter = 0;
    Widget TestDisable = null;
    
    public void vvvInit() throws IOException
    {  
        vvvInitTexture();
        vvvInitAnimation();
        
        screen = new Screen();
        SpriteLayer backGroundLayer = new SpriteLayer();
            
        screen.addLayer(backGroundLayer);
            backGroundLayer.setShader( defaults.Defaults.getSpriteShader() );

            Random r = new Random();

            for(int i=0; i < 20000; ++i)
            {
                StaticSprite     spr = new StaticSprite();
                tll = texlist[i%10];
                spr.setTexture( tll );
                spr.setScale( tll.getWidth(), tll.getHeight(), 1 );
                spr.setPosition( (r.nextInt()%DISPLAY_WIDTH * 20 ), 
                                 (r.nextInt()%DISPLAY_HEIGHT* 20 ),
                                 0);
                backGroundLayer.addObject(spr);
            }
        
        SpriteLayer frontLayer = new SpriteLayer();
            frontLayer.setShader( defaults.Defaults.getSpriteShader() );
            screen.addLayer(frontLayer);
            sprite1 = new AnimatedSprite();
                sprite1.setPosition( 0, 0, 0);
                sprite1.setScale( 128, 128, 1);
                sprite1.setAnimation(animationIdle);
                sprite1.playAnimation();

            frontLayer.addObject(sprite1);
            
        
        bgCamera    = backGroundLayer.getCamera();
        frontCamera = frontLayer.getCamera();
        vvvInitGui(screen);
        bgSpriteLayer = backGroundLayer;
    }
    SpriteLayer bgSpriteLayer=null; 
    
    
    public void initGL()
    {
        //2D Initialization
       // glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearColor(0.0f, 0.1f, 0.2f, 1.0f);
        //glEnable(GL_DEPTH_TEST);
        try
        {
            vvvInit();
        }
        catch (IOException ex)
        {
            Logger.getLogger(VVVEngine.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    private DefaultHorizontalSlider slider;
    private TextLabel tl = null;
    private String texts = "";
    
    private final float velocity = 250;
    public void processKeyboard()
    {   
 
        while( Keyboard.next() )
        {
            char c = Keyboard.getEventCharacter();
            int  i = Keyboard.getEventKey();
            
            if( Keyboard.getEventKeyState() )
            {    
                System.out.println("+ " + c + " " + i);
                tl.setText( texts + c );
                texts = texts + c;
            }
            else
            {
                System.out.println("- " + c + " " + i);
            }
            
            if( i == Keyboard.KEY_V )
            {
                Display.setVSyncEnabled(true);
            }
            else if( i == Keyboard.KEY_B)
            {
                Display.setVSyncEnabled(false);
            }
            
        }        
    }

    
    Vector2f  tvup = new Vector2f(0, 1);
    Vector2f  mouse = new Vector2f();
    Vector2f  direction = new Vector2f(0,1);
    
    boolean   lmbDown = false;
    Vector2f  guiMouse = new Vector2f();
    Vector2f  lastMouse = new Vector2f();
    
    public void processMouse()
    {
        guiMouse.set(Mouse.getX(), Mouse.getY()); 
        mouse.x = Mouse.getX() - DISPLAY_WIDTH/2  ;
        mouse.y = Mouse.getY() - DISPLAY_HEIGHT/2 ;
        
        WidgetLayer wl = screen.getGuiLayer();
        
        if( Mouse.isButtonDown(0) )
        {
            if( lmbDown == false )
            {
                wl.onLeftMouseButtonDown(guiMouse.x, guiMouse.y);
                lmbDown = true;
            }
        }
        else
        {
            if( lmbDown == true)
            {
                wl.onLeftMouseButtonUp(guiMouse.x, guiMouse.y);
                lmbDown = false;
            }
        }
        
        if( !lastMouse.equals( guiMouse ) )
        {
            lastMouse.set(guiMouse);
            wl.onMouseMove( guiMouse.x, guiMouse.y );
        }

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
            float dtf;
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
            bgCamera.move(direction.x * s, direction.y * s, 0);
            frontCamera.move(direction.x * s, direction.y * s, 0);
            sprite1.setAnimation(animationRotation);
        }
        else
        {
           MB0Pressed = false;
           sprite1.setAnimation(animationIdle); 
        }
        
        int dw = Mouse.getDWheel();
        if( dw != 0 )
        {
            if( dw > 0 )
            {
               zoom *= 1.1;
            }
            else
            {
                zoom /= 1.1;
            } 
            float top   = DISPLAY_HEIGHT / 2 * zoom;
            float botom = -top;
            float left  = -DISPLAY_WIDTH / 2 * zoom;
            float right = -left;
            
            bgCamera.setOrtho( top, botom, left, right, -1, 1);
            frontCamera.setOrtho( top, botom, left, right, -1, 1);
        }
       // sprite1.setPosition( squareX-DISPLAY_WIDTH/2, squareY-DISPLAY_HEIGHT/2, 0 );
    }
    private float zoom = 1.0f;
    
    private boolean MB0Pressed = false;
    private long    timeLastPressed = 0;
    
    
    public void render() throws TextureNotLoadedException
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);    
       
        // Scissor test
       // glScissor( 50, 50, 400, 400);
       // glEnable(GL_SCISSOR_TEST);
       // glDisable(GL_SCISSOR_TEST);
        
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
static int frameCounter = 0;


    public void update()
    {
    
    }
    
    public Screen screen;
    public AnimatedSprite sprite1;
    public Camera bgCamera;
    public Camera frontCamera;
    public SpriteAnimation animationIdle;
    public SpriteAnimation animationRotation;
}

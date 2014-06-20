package vvv.engine.widgets;

import defaults.Defaults;
import org.lwjgl.util.vector.Matrix4f;
import vvv.engine.Camera;
import vvv.engine.Geometry;
import vvv.engine.shader.ModelShader;
import vvv.engine.sprite.SpriteGeometry;
import vvv.engine.sprite.StaticSprite;
import vvv.engine.texture.Texture;

/**
 *
 * @author vvv
 */
public class ImageWidget extends Widget
{
    private final StaticSprite       sprite;  
    
    public ImageWidget()
    {
        sprite = new StaticSprite();
    }
    
    public ImageWidget( Texture tex)
    {
        this();
        setTexture(tex);
        final int imageHeight = tex.getHeight();
        final int imageWidth  = tex.getWidth();
        setSize(imageWidth, imageHeight);  
    }
    
    public final void setTexture( Texture tex)
    {
        sprite.setTexture( tex ); 
    }
    
    
    @Override
    protected void onDraw() throws Exception
    {
        final ModelShader sh = Defaults.getSpriteShader();
        final Camera camera  = getCamera();
        
        sh.activate();
        sh.setTexture(0, sprite.getTexture() );
        Matrix4f.mul( camera.getViewProjectionMatrix4f(), 
                      position.getMatrix4f(), 
                      tmp);
        sh.setModelViewProjectionMatrix(tmp);
        final Geometry gm = SpriteGeometry.getGeometry();
        gm.activate();
        defaults.Defaults.enableTransparency();
        gm.draw();    
        defaults.Defaults.disableTransparency();
    }
    private static final Matrix4f tmp = new Matrix4f();

    private void repositionBySize()
    {
        final int w = getWidth();
        final int w_2 = (w % 2 == 1) ? w+1 : w;
        
        final int h_2 = getHeight();

        position.setPosition(   (float)(getGlobalPosX() + w_2*0.5f ),
                                (float)(getGlobalPosY() + h_2* 0.5f ),
                                0);
    }
    
    @Override
    protected void onSetPosition(float x, float y)
    {
        repositionBySize();
    }

    @Override
    protected void onSetSize(int w, int h)
    {
        position.setScale(w, h, 1);
        repositionBySize();
    }
}

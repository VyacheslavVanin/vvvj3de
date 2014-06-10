package vvv.engine.widgets;

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
        final float imageHeight = tex.getHeight();
        final float imageWidth  = tex.getWidth();
        setSize(imageWidth, imageHeight);  
    }
    
    public final void setTexture( Texture tex)
    {
        sprite.setTexture( tex ); 
    }
    
    
    @Override
    protected void onDraw() throws Exception
    {
        final ModelShader sh = getImageShader();
        final Camera camera  = getCamera();
        
        sh.activate();
        sh.setTexture(0, sprite.getTexture() );
        Matrix4f.mul( camera.getViewProjectionMatrix4f(), 
                      position.getMatrix4f(), 
                      tmp);
        sh.setModelViewProjectionMatrix(tmp);
        final Geometry gm = SpriteGeometry.getGeometry();
        gm.activate();
        gm.draw();    
    }
    private static final Matrix4f tmp = new Matrix4f();

    private void repositionBySize()
    {
        position.setPosition(   (float) Math.floor(getGlobalPosX() + getWidth()  / 2 ),
                                (float) Math.floor(getGlobalPosY() + getHeight() / 2 ),
                                0);
    }
    
    @Override
    protected void onSetPosition(float x, float y)
    {
        repositionBySize();
    }

    @Override
    protected void onSetSize(float w, float h)
    {
        position.setScale(w, h, 1);
        repositionBySize();
    }
}

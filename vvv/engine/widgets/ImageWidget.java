/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.widgets;

import org.lwjgl.util.vector.Matrix4f;
import vvv.engine.Camera;
import vvv.engine.Geometry;
import vvv.engine.layers.PositionProperties;
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
    private StaticSprite       sprite = null;
    private PositionProperties position = new PositionProperties();
    private float              imageWidth = 0;
    private float              imageHeight = 0;
    
    public ImageWidget()
    {
       sprite = new StaticSprite();
    }
    
    public void setTexture( Texture tex)
    {
        sprite.setTexture( tex );
        imageHeight = tex.getHeight();
        imageWidth  = tex.getWidth();
        setHeight(imageHeight);
        setWidth(imageWidth);
        position.setScale(imageWidth, imageHeight, 1);
    }
    
    public void setPosition( float x, float y)
    {
        this.setPosX(x);
        this.setPosY(y);
        position.setPosition( getGlobalPosX() + imageWidth/2, 
                              getGlobalPosY() + imageHeight/2,
                              0);
    }
    
    @Override
    public void onDraw() throws Exception
    {
        ModelShader sh = getImageShader();
        Camera camera  = getCamera();
        
        sh.activate();
        sh.setTexture(0, sprite.getTexture() );
        Matrix4f.mul( camera.getViewProjectionMatrix4f(), 
                      position.getMatrix4f(), 
                      tmp);
        sh.setModelViewProjectionMatrix(tmp);
        Geometry gm = SpriteGeometry.getGeometry();
        gm.activate();
        gm.draw();    
    }
    private static Matrix4f tmp = new Matrix4f();   
}

package vvv.engine.shader;

import vvv.engine.texture.Texture;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import vvv.engine.texture.TextureLowLevel.TextureNotLoadedException;

public class ShaderSprite extends Shader
{

    private int location_modelViewProjectionMatrix;
    private int location_modelMatrix;
    private int location_texture;
    private int location_textureCoordData;
    private FloatBuffer fb;
    public static final String UNIFORM_NAME_MODEL_VIEW_PROJECTION_MATRIX = "MVPMatrix";
    public static final String UNIFORM_NAME_MODEL_MATRIX = "MMatrix";
    public static final String UNIFORM_NAME_TEXTURE0 = "Texture0";
    public static final String UNIFORM_NAME_TEXTURE_COORDINATE_DATA = "TexCoordData";

    public ShaderSprite()
    {
        ByteBuffer bytes = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder());
        fb = bytes.asFloatBuffer();
        fb.position(0);
    }

    @Override
    public void updateLocations()
    {
        int prog = getProgram();
        location_modelMatrix =
                glGetUniformLocation(prog, UNIFORM_NAME_MODEL_MATRIX);
        location_modelViewProjectionMatrix =
                glGetUniformLocation(prog, UNIFORM_NAME_MODEL_VIEW_PROJECTION_MATRIX);
        location_texture =
                glGetUniformLocation(prog, UNIFORM_NAME_TEXTURE0);
        location_textureCoordData =
                glGetUniformLocation(prog, UNIFORM_NAME_TEXTURE_COORDINATE_DATA);

    }

    @Override
    public void clearLocations()
    {
        location_modelMatrix = -1;
        location_modelViewProjectionMatrix = -1;
        location_texture = -1;
        location_textureCoordData = -1;
    }

    public boolean setModelViewProjectionMatrix(Matrix4f m)
    {
        if (location_modelViewProjectionMatrix == -1)
        {
            return false;
        }
        m.store(fb);
        glUniformMatrix4(location_modelViewProjectionMatrix, false, fb);
        return true;
    }

    public boolean setModelMatrix(Matrix4f m)
    {
        if (location_modelMatrix == -1)
        {
            return false;
        }
        m.store(fb);
        glUniformMatrix4(location_modelMatrix, false, fb);
        return true;
    }

    public boolean setTexture(Texture tex) throws TextureNotLoadedException
    {
        if (location_texture == -1)
        {
            return false;
        }
        if (location_textureCoordData == -1)
        {
            return false;
        }

        tex.getTexture().activate(0);
        glUniform1i(location_texture, 0);
        Vector4f v = tex.getTexCoord().get();
        glUniform4f(location_textureCoordData, v.x, v.y, v.z, v.w);
        return true;
    }
}

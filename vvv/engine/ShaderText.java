package vvv.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import static vvv.engine.Constants.NUM_TEXTURE_UNITS;
import vvv.engine.TextureLowLevel.TextureNotLoadedException;

public class ShaderText extends Shader
{

    private int location_modelViewProjectionMatrix;
    private int location_modelMatrix;
    private int[] location_texture = new int[NUM_TEXTURE_UNITS];
    private int location_textColor;
    private int location_textureCoordData;
    private FloatBuffer fb;
    public static final String UNIFORM_NAME_MODEL_VIEW_PROJECTION_MATRIX = "MVPMatrix";
    public static final String UNIFORM_NAME_MODEL_MATRIX = "MMatrix";
    public static final String UNIFORM_NAME_TEXTURE = "Texture";
    public static final String UNIFORM_NAME_TEXT_COLOR = "TextColor";
    public static final String UNIFORM_NAME_TEXTURE_COORDINATE_DATA = "TexCoordData";

    public ShaderText()
    {
        ByteBuffer bytes = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder());
        fb = bytes.asFloatBuffer();
        fb.position(0);
    }

    @Override
    public void updateLocations()
    {
        int prog = getProgram();
        location_modelViewProjectionMatrix =
                glGetUniformLocation(prog, UNIFORM_NAME_MODEL_VIEW_PROJECTION_MATRIX);
        location_modelMatrix =
                glGetUniformLocation(prog, UNIFORM_NAME_MODEL_MATRIX);

        for (int i = 0; i < NUM_TEXTURE_UNITS; ++i)
        {
            location_texture[i] = glGetUniformLocation(prog, UNIFORM_NAME_TEXTURE + i);
        }

        location_textureCoordData =
                glGetUniformLocation(prog, UNIFORM_NAME_TEXTURE_COORDINATE_DATA);

        location_textColor =
                glGetUniformLocation(prog, UNIFORM_NAME_TEXT_COLOR);
    }

    @Override
    public void clearLocations()
    {
        location_modelViewProjectionMatrix = -1;
        location_modelMatrix = -1;
        for (int i = 0; i < NUM_TEXTURE_UNITS; ++i)
        {
            location_texture[i] = -1;
        }
        location_textColor = -1;
        location_textureCoordData = -1;
    }

    public boolean setMoodelViewProjectionMatrix(Matrix4f m)
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

    public boolean setTexture(int unit, Texture tex) throws TextureNotLoadedException
    {
        if (location_texture[unit] == -1)
        {
            return false;
        }
        if (location_textureCoordData == -1)
        {
            return false;
        }

        tex.getTexture().activate(unit);
        glUniform1i(location_texture[ unit], unit);
        Vector4f v = tex.getTexCoord().get();
        glUniform4f(location_textureCoordData, v.x, v.y, v.z, v.w);
        return true;
    }

  
    public boolean setMaterialDiffuseColor(Vector4f v)
    {
        if (location_textColor == -1)
        {
            return false;
        }
        glUniform4f(location_textColor, v.x, v.y, v.z, v.w);
        return true;
    }
}

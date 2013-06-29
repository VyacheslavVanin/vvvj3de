package vvv.engine;

//import static android.opengl.GLES20.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import vvv.engine.TextureLowLevel.TextureNotLoadedException;

public class ShaderModel extends Shader
{

    private int location_modelViewProjectionMatrix;
    private int location_modelMatrix;
    private int location_modelNormalMatrix;
    private int location_viewPosition;
    private int[] location_texture = new int[Constants.NUM_TEXTURE_UNITS];
    private int[] location_color = new int[Constants.NUM_COLOR_UNIFORMS];
    private int location_textureCoordData;
    private int location_materialDiffuseColor;
    private int location_materialDiffuseIntensity;
    private int location_materialSpecularColor;
    private int location_materialSpecularIntensity;
    private int location_materialSpecularHardness;
    private int location_materialEmit;
    private FloatBuffer fb;
    public static final String UNIFORM_NAME_MODEL_VIEW_PROJECTION_MATRIX = "MVPMatrix";
    public static final String UNIFORM_NAME_MODEL_MATRIX = "MMatrix";
    public static final String UNIFORM_NAME_NORMAL_MATRIX = "NormalMatrix";
    public static final String UNIFORM_NAME_VIEW_POSITION = "ViewPosition";
    public static final String UNIFORM_NAME_COLOR   = "Color";
    public static final String UNIFORM_NAME_TEXTURE = "Texture";
    public static final String UNIFORM_NAME_TEXTURE_COORDINATE_DATA = "TexCoordData";
    public static final String UNIFORM_NAME_MATERIAL_DIFFUSE_COLOR = "DiffuseColor";
    public static final String UNIFORM_NAME_MATERIAL_DIFFUSE_INTENSITY = "DiffuseIntensity";
    public static final String UNIFORM_NAME_MATERIAL_SPECULAR_COLOR = "SpecularColor";
    public static final String UNIFORM_NAME_MATERIAL_SPECULAR_INTENSITY = "SpecularIntensity";
    public static final String UNIFORM_NAME_MATERIAL_SPECULAR_HARDNESS = "SpecularHardness";
    public static final String UNIFORM_NAME_MATERIAL_EMIT = "Emit";
   

    public ShaderModel()
    {
        fb = BufferUtils.createFloatBuffer(16);
        //fb.flip();
    }

    @Override
    public void updateLocations()
    {
        int prog = getProgram();
        location_modelViewProjectionMatrix =
                glGetUniformLocation(prog, UNIFORM_NAME_MODEL_VIEW_PROJECTION_MATRIX);
        location_modelMatrix =
                glGetUniformLocation(prog, UNIFORM_NAME_MODEL_MATRIX);
        location_modelNormalMatrix =
                glGetUniformLocation(prog, UNIFORM_NAME_NORMAL_MATRIX);
        location_viewPosition =
                glGetUniformLocation(prog, UNIFORM_NAME_VIEW_POSITION);

        for (int i = 0; i < Constants.NUM_TEXTURE_UNITS; ++i)
        {
            location_texture[i] = glGetUniformLocation(prog, UNIFORM_NAME_TEXTURE + i);
        }

        for( int i = 0; i < Constants.NUM_COLOR_UNIFORMS; ++i)
        {
            location_color[i] = glGetUniformLocation(prog, UNIFORM_NAME_COLOR + i);
        }
        
        location_textureCoordData = 
				glGetUniformLocation( prog, UNIFORM_NAME_TEXTURE_COORDINATE_DATA);
        location_materialDiffuseColor =
                glGetUniformLocation(prog, UNIFORM_NAME_MATERIAL_DIFFUSE_COLOR);
        location_materialDiffuseIntensity =
                glGetUniformLocation(prog, UNIFORM_NAME_MATERIAL_DIFFUSE_INTENSITY);
        location_materialSpecularColor =
                glGetUniformLocation(prog, UNIFORM_NAME_MATERIAL_SPECULAR_COLOR);
        location_materialSpecularIntensity =
                glGetUniformLocation(prog, UNIFORM_NAME_MATERIAL_SPECULAR_INTENSITY);
        location_materialSpecularHardness =
                glGetUniformLocation(prog, UNIFORM_NAME_MATERIAL_SPECULAR_HARDNESS);
        location_materialEmit =
                glGetUniformLocation(prog, UNIFORM_NAME_MATERIAL_EMIT);
    }

    @Override
    public void clearLocations()
    {
        location_modelViewProjectionMatrix = -1;
        location_modelMatrix = -1;
        location_modelNormalMatrix = -1;
        for (int i = 0; i < Constants.NUM_TEXTURE_UNITS; ++i)
        {
            location_texture[i] = -1;
        }
        
        for( int i = 0; i < Constants.NUM_COLOR_UNIFORMS; ++i)
        {
            location_color[i] = -1;
        }
        
        location_textureCoordData = -1;
        location_materialDiffuseColor = -1;
        location_materialDiffuseIntensity = -1;
        location_materialSpecularColor = -1;
        location_materialSpecularIntensity = -1;
        location_materialSpecularHardness = -1;
        location_materialEmit = -1;
    }

    public boolean setMoodelViewProjectionMatrix(Matrix4f m)
    {
        if (location_modelViewProjectionMatrix == -1)
        {
            return false;
        }
        m.store(fb);
        fb.position(0);
        glUniformMatrix4(location_modelViewProjectionMatrix, true, fb);
        return true;
    }

    public boolean setModelMatrix(Matrix4f m)
    {
        if (location_modelMatrix == -1)
        {
            return false;
        }
        m.store(fb);
        fb.position(0);
        glUniformMatrix4(location_modelMatrix, true, fb);
        return true;
    }

    public boolean setNormalMatrix(Matrix3f m)
    {
        if (location_modelNormalMatrix == -1)
        {
            return false;
        }
        m.store(fb);
        fb.position(0);
        glUniformMatrix3(location_modelNormalMatrix, true, fb);
        return true;
    }

    public boolean setViewPosition(Vector3f v)
    {
        if (location_viewPosition == -1)
        {
            return false;
        }
        glUniform3f(location_viewPosition, v.x, v.y, v.z);
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

        if( tex.getTexture().activate(unit) )
        {
            glUniform1i(location_texture[ unit], unit);
        }
        Vector4f v = tex.getTexCoord().get();
        glUniform4f(location_textureCoordData, v.x, v.y, v.z, v.w);
        return true;
    }
    
    public boolean setColor( int colorUnit, Vector4f v)
    {
        if( location_color[colorUnit] == -1 )
        {
            return false;
        }
        glUniform4f(location_color[colorUnit], v.x, v.y, v.z, v.w);
        return true;
    }

    public boolean setMaterialDiffuseColor(Vector4f v)
    {
        if (location_materialDiffuseColor == -1)
        {
            return false;
        }
        glUniform4f(location_materialDiffuseColor, v.x, v.y, v.z, v.w);
        return true;
    }

    public boolean setMaterialDiffuseIntensity(float intensity)
    {
        if (location_materialDiffuseIntensity == -1)
        {
            return false;
        }

        glUniform1f(location_materialDiffuseIntensity, intensity);
        return true;
    }

    public boolean setMaterialSpecularColor(Vector4f v)
    {
        if (location_materialSpecularColor == -1)
        {
            return false;
        }
        glUniform4f(location_materialSpecularColor, v.x, v.y, v.z, v.w);
        return true;
    }

    public boolean setMaterialSpecularIntensity(float intensity)
    {
        if (location_materialSpecularIntensity == -1)
        {
            return false;
        }

        glUniform1f(location_materialSpecularIntensity, intensity);
        return true;
    }

    public boolean setMaterialSpecularHardness(float hardness)
    {
        if (location_materialSpecularHardness == -1)
        {
            return false;
        }

        glUniform1f(location_materialSpecularHardness, hardness);
        return true;
    }

    public boolean setMaterialEmit(float emit)
    {
        if (location_materialEmit == -1)
        {
            return false;
        }

        glUniform1f(location_materialEmit, emit);
        return true;
    }
}

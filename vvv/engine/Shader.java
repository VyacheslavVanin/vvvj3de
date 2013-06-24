package vvv.engine;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static vvv.engine.Constants.*;

public abstract class Shader
{
    private int prog;
    private int vsh;
    private int fsh;
    public static final String ATTRIBUTE_VERTEX_POSITION_NAME = "vPosition";
    public static final String ATTRIBUTE_VERTEX_NORMAL_NAME = "vNormal";
    public static final String ATTRIBUTE_VERTEX_TEXCOORD_NAME = "vTexCoord";
    public static final String ATTRIBUTE_VERTEX_BINORMAL_NAME = "vBinormal";
    public static final String ATTRIBUTE_VERTEX_TANGENT_NAME = "vTangent";

    private boolean shaderStatus(int shader, int param)
    {
        int status = glGetShaderi(shader, param);

        if (status != GL_TRUE)
        {
            System.out.println("vvv3d error: "
                               + glGetShaderInfoLog(shader, 256));
            return false;
        }

        return true;
    }

    private boolean shaderProgramStatus(int prog, int param)
    {
        int status = glGetProgrami(prog, param);

        if (status != GL_TRUE)
        {
            System.out.println("vvv3d error: "
                                + glGetProgramInfoLog(prog, 256));
            return false;
        }

        return true;
    }

    private void clear()
    {
        if (prog != -1)
        {
            glDeleteProgram(prog);
            prog = -1;
        }
        if (vsh != -1)
        {
            glDeleteShader(vsh);
            vsh = -1;
        }
        if (fsh != -1)
        {
            glDeleteShader(fsh);
            fsh = -1;
        }
    }

    private void attributesBindings(int prog)
    {
        glBindAttribLocation(prog, ATTRIBUTE_VERTEX_POSITION, ATTRIBUTE_VERTEX_POSITION_NAME);
        glBindAttribLocation(prog, ATTRIBUTE_VERTEX_NORMAL, ATTRIBUTE_VERTEX_NORMAL_NAME);
        glBindAttribLocation(prog, ATTRIBUTE_VERTEX_TEXCOORD, ATTRIBUTE_VERTEX_TEXCOORD_NAME);
        glBindAttribLocation(prog, ATTRIBUTE_VERTEX_BINORMAL, ATTRIBUTE_VERTEX_BINORMAL_NAME);
        glBindAttribLocation(prog, ATTRIBUTE_VERTEX_TANGENT, ATTRIBUTE_VERTEX_TANGENT_NAME);
    }

    abstract public void updateLocations();

    abstract public void clearLocations();

    public int getProgram()
    {
        return prog;
    }

    public int getAttribLocation(String str)
    {
        return glGetAttribLocation(prog, str);
    }

    public int getUniformLocation(String str)
    {
        return glGetUniformLocation(prog, str);
    }

    public boolean load(String vertexShaderSource, String fragmentShaderSource)
    {
        int tvsh = glCreateShader(GL_VERTEX_SHADER);
        if (tvsh == 0)
        {
            return false;
        }

        int tfsh = glCreateShader(GL_FRAGMENT_SHADER);
        if (tfsh == 0)
        {
            glDeleteShader(tvsh);
            return false;
        }

        int tprog = glCreateProgram();
        if (tprog == 0)
        {
            glDeleteShader(tvsh);
            glDeleteShader(tfsh);
            return false;
        }

        glShaderSource(tvsh, vertexShaderSource);
        glCompileShader(tvsh);

        if (!shaderStatus(tvsh, GL_COMPILE_STATUS))
        {
            glDeleteShader(tvsh);
            glDeleteShader(tfsh);
            glDeleteProgram(tprog);
            return false;
        }

        glShaderSource(tfsh, fragmentShaderSource);
        glCompileShader(tfsh);
        if (!shaderStatus(tfsh, GL_COMPILE_STATUS))
        {
            glDeleteShader(tvsh);
            glDeleteShader(tfsh);
            glDeleteProgram(tprog);
            return false;
        }

        glAttachShader(tprog, tfsh);
        glAttachShader(tprog, tvsh);

        attributesBindings(tprog);

        glLinkProgram(tprog);

        if (shaderProgramStatus(tprog, GL_LINK_STATUS) == false)
        {
            glDeleteShader(tvsh);
            glDeleteShader(tfsh);
            glDeleteProgram(tprog);
            return false;
        }

        glValidateProgram(tprog);
        if (shaderProgramStatus(tprog, GL_VALIDATE_STATUS) == false)
        {
            glDeleteShader(tvsh);
            glDeleteShader(tfsh);
            glDeleteProgram(tprog);
            return false;
        }

        clear();

        prog = tprog;
        vsh = tvsh;
        fsh = tfsh;

        updateLocations();

        return true;
    }

    public void activate()
    {
        glUseProgram(prog);
    }

    Shader()
    {
        prog = -1;
        vsh = -1;
        fsh = -1;
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        clear();
    }
}

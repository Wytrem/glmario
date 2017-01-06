package net.wytrem.glmario.gl.shaders;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

public class Shader
{
    public static final String FOLDER = "/shaders/";
    
    public static enum Type
    {
        VERTEX_SHADER(GL_VERTEX_SHADER),
        FRAGMENT_SHADER(GL_FRAGMENT_SHADER);
        
        private int glConstant;

        private Type(int glConstant)
        {
            this.glConstant = glConstant;
        }
        
        public int getGlConstant()
        {
            return glConstant;
        }
    }
    
    private int shaderId;

    public Shader(Type type, String source)
    {
        shaderId = glCreateShader(type.getGlConstant());
        glShaderSource(shaderId, source);
    }
    
    public Shader compile()
    {
        glCompileShader(shaderId);
        
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) != GL_TRUE)
        {
            throw new ShaderCompileException(glGetShaderInfoLog(shaderId));
        }
        
        return this;
    }
    

    public void delete()
    {
        glDeleteShader(shaderId);
    }
    
    public int getId()
    {
        return shaderId;
    }
}

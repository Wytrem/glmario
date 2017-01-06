package net.wytrem.glmario.gl.shaders;

import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.wytrem.glmario.utils.MatrixStack;

public class ShaderProgram
{
    private int programId;
    private TObjectIntMap<String> uniforms = new TObjectIntHashMap<>();
    
    public ShaderProgram()
    {
        programId = glCreateProgram();
    }
    
    public void attach(Shader shader)
    {
        glAttachShader(programId, shader.getId());
    }
    
    public void link()
    {
        glLinkProgram(programId);
    }
    
    public void use()
    {
        glUseProgram(programId);
        MatrixStack.changeProgram(this);
    }
    
    public void setUniformMatrix4f(String uniform, Matrix4f matrix)
    {
        if (hasUniform(uniform))
        {
           FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
           matrix.get(buffer);
        }
    }
    
    public int getUniformLocation(String uniform)
    {
        if (hasUniform(uniform))
        {
            return uniforms.get(uniform);
        }
        
        return glGetUniformLocation(programId, uniform);
    }
    
    public boolean hasUniform(String uniform)
    {
        return uniforms.containsKey(uniform);
    }

    public static ShaderProgram create(String vertexSource, String fragmentSource, String...uniforms)
    {
        ShaderProgram program = new ShaderProgram();
        
        VertexShader vertexShader = new VertexShader(vertexSource);
        vertexShader.compile();
        
        FragmentShader fragmentShader = new FragmentShader(fragmentSource);
        fragmentShader.compile();
        
        program.attach(vertexShader);
        program.attach(fragmentShader);
        
        program.link();
        
        
        for (String uniform : uniforms)
        {
            program.uniforms.put(uniform, glGetUniformLocation(program.programId, uniform));
        }
        
        vertexShader.delete();
        fragmentShader.delete();
        
        return program;
    }
}

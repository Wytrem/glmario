package net.wytrem.glmario.gl.buffers;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class VertexBufferObject
{
    private static final int TARGET = GL_ARRAY_BUFFER;
    
    private int bufferId;
    
    public VertexBufferObject()
    {
        bufferId = glGenBuffers();
    }
    
    public void bufferData(FloatBuffer buffer, BufferUsage usage)
    {
        bind();
        glBufferData(TARGET, buffer, usage.getGlConstant());
        bindNone();
    }
    
    public void bufferData(ByteBuffer buffer, BufferUsage usage)
    {
        bind();
        glBufferData(TARGET, buffer, usage.getGlConstant());
        bindNone();
    }
    
    public void bind()
    {
        glBindBuffer(TARGET, bufferId);
    }
    
    public int getVboId()
    {
        return bufferId;
    }
    
    public static void bindNone()
    {
        glBindBuffer(TARGET, 0);
    }
}

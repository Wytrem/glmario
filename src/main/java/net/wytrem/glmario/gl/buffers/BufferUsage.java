package net.wytrem.glmario.gl.buffers;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_COPY;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.GL_STATIC_COPY;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_READ;
import static org.lwjgl.opengl.GL15.GL_STREAM_COPY;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_READ;

public enum BufferUsage
{
    STREAM_DRAW(GL_STREAM_DRAW),
    STREAM_COPY(GL_STREAM_COPY), 
    STREAM_READ(GL_STREAM_READ),
    DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
    DYNAMIC_COPY(GL_DYNAMIC_COPY), 
    DYNAMIC_READ(GL_DYNAMIC_READ),
    STATIC_DRAW(GL_STATIC_DRAW),
    STATIC_COPY(GL_STATIC_COPY),
    STATIC_READ(GL_STATIC_READ);
    

    private final int glConstant;

    private BufferUsage(int glConstantIn)
    {
        this.glConstant = glConstantIn;
    }

    public int getGlConstant()
    {
        return this.glConstant;
    }
}
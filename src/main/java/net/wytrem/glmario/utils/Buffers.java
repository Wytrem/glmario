package net.wytrem.glmario.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Buffers
{

    public static IntBuffer createFlippedIntBuffer(int... data)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        for (int i = 0; i < data.length; i++)
        {
            buffer.put(data[i]);
        }
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer createFlippedByteBuffer(byte... data)
    {
        ByteBuffer buf = BufferUtils.createByteBuffer(data.length);
        for (int i = 0; i < data.length; i++)
        { 
            buf.put(data[i]);
        }
        buf.flip();
        return buf;
    }
    
    public static ByteBuffer createFlippedByteBuffer(int... data)
    {
        ByteBuffer buf = BufferUtils.createByteBuffer(data.length);
        for (int i = 0; i < data.length; i++)
        {
            buf.put((byte) data[i]);
        }
        buf.flip();
        return buf;
    }

    public static FloatBuffer createFlippedFloatBuffer(float... data)
    {
        FloatBuffer buf = BufferUtils.createFloatBuffer(data.length);
        for (int i = 0; i < data.length; i++)
        {
            buf.put(data[i]);
        }
        buf.flip();
        return buf;
    }
}

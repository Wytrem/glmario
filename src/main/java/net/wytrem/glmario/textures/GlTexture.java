package net.wytrem.glmario.textures;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class GlTexture
{
    public static final int TARGET = GL_TEXTURE_2D;

    private int textureId;

    private int width, height;
    
    public GlTexture(InputStream stream) throws IOException
    {
        this(ImageIO.read(stream));
    }

    public GlTexture(BufferedImage image)
    {
        width = image.getWidth();
        height = image.getHeight();
        textureId = glGenTextures();

        upload(image);
    }

    public void upload(BufferedImage image)
    {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);

        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xff));
                buffer.put((byte) ((pixel >> 8) & 0xff));
                buffer.put((byte) ((pixel) & 0xff));
                buffer.put((byte) ((pixel >> 24) & 0xff));
            }
        }

        // DO NOT forget
        buffer.flip();

        texImage2d(image.getWidth(), image.getHeight(), buffer);
        
        parameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        parameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        parameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        parameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    public void texImage2d(int width, int height, ByteBuffer buffer)
    {
        bind();
        glTexImage2D(TARGET, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        bindNone();
    }
    
    public void parameter(int pname, int param)
    {
        bind();
        glTexParameteri(TARGET, pname, param);
        bindNone();
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }

    public void bind()
    {
        glBindTexture(TARGET, textureId);
    }

    public static void bindNone()
    {
        glBindTexture(TARGET, 0);
    }

    public int getId()
    {
        return textureId;
    }

    public void dispose()
    {
        glDeleteTextures(textureId);
    }
    
    static
    {
        try
        {
            throw new RuntimeException();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
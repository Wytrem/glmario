package net.wytrem.glmario.gl.buffers;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.List;

import net.wytrem.glmario.gl.format.VertexFormat;
import net.wytrem.glmario.gl.format.VertexFormatElement;

public class VertexArrayObject
{
    private int vaoId;
    
    public VertexArrayObject()
    {
        vaoId = glGenVertexArrays();
    }
    
    public void prepare(VertexFormat vertexFormat, VertexBufferObject vbo)
    {
        bind();
        {
            vbo.bind();
            enableArrays(vertexFormat);
        }
        bindNone();
    }
    
    private void enableArrays(VertexFormat vertexFormat)
    {
        int stride = vertexFormat.getStride();
        List<VertexFormatElement> elements = vertexFormat.getElements();

        for (int i = 0; i < elements.size(); ++i)
        {
            VertexFormatElement element = elements.get(i);
            glVertexAttribPointer(i, element.getElementCount(), element.getType().getGlConstant(), false, stride, vertexFormat.getOffset(i));
            glEnableVertexAttribArray(i);
        }
    }
    
    public void bind()
    {
        glBindVertexArray(vaoId);
    }
    
    public static void bindNone()
    {
        glBindVertexArray(0);
    }
    
    public int getVaoId()
    {
        return vaoId;
    }

    public void prepare(VertexFormat vertexFormat, VertexBufferObject vbo, ElementBufferObject ebo)
    {
        bind();
        {
            vbo.bind();
            ebo.bind();
            enableArrays(vertexFormat);
        }
        bindNone();
    }
}

package net.wytrem.glmario.gl.render;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;

import net.wytrem.glmario.gl.buffers.BufferUsage;
import net.wytrem.glmario.gl.buffers.ElementBufferObject;
import net.wytrem.glmario.gl.buffers.VertexArrayObject;
import net.wytrem.glmario.gl.buffers.VertexBuffer;
import net.wytrem.glmario.gl.buffers.VertexBufferObject;


public class DirectRenderer
{
    VertexArrayObject vao;
    VertexBufferObject vbo;
    ElementBufferObject ebo;

    public DirectRenderer()
    {
        vao = new VertexArrayObject();
        vbo = new VertexBufferObject();
        ebo = new ElementBufferObject();
    }

    public void directRender(VertexBuffer vertexBuffer)
    {
        if (vertexBuffer.getVertexCount() > 0)
        {
            vbo.bufferData(vertexBuffer.getVertexBuffer(), BufferUsage.STREAM_DRAW);
            
            if (vertexBuffer.getDrawMode().doesUseIndices())
            {
                vao.prepare(vertexBuffer.getVertexFormat(), vbo, ebo);
                ebo.bufferData(vertexBuffer.getIndicesBuffer(), BufferUsage.STREAM_DRAW);
            }
            else
            {
                vao.prepare(vertexBuffer.getVertexFormat(), vbo);
            }

            vao.bind();
            {
                if (vertexBuffer.getDrawMode().doesUseIndices())
                {
                    glDrawElements(vertexBuffer.getDrawMode().getGlConstant(), vertexBuffer.getIndicesCount(), vertexBuffer.getIndicesType().getGlConstant(), 0l);
                }
                else
                {
                    glDrawArrays(vertexBuffer.getDrawMode().getGlConstant(), 0, vertexBuffer.getVertexCount());
                }
            }
            VertexArrayObject.bindNone();
        }

        vertexBuffer.reset();
    }

}

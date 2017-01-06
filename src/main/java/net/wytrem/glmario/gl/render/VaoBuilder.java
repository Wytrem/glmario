package net.wytrem.glmario.gl.render;

import net.wytrem.glmario.gl.buffers.BufferUsage;
import net.wytrem.glmario.gl.buffers.ElementBufferObject;
import net.wytrem.glmario.gl.buffers.VertexArrayObject;
import net.wytrem.glmario.gl.buffers.VertexBuffer;
import net.wytrem.glmario.gl.buffers.VertexBufferObject;


public class VaoBuilder implements RenderableProvider
{
    @Override
    public Renderable provide(VertexBuffer vertexBuffer)
    {
        if (vertexBuffer.getVertexCount() > 0)
        {
            VertexArrayObject vao = new VertexArrayObject();
            VertexBufferObject vbo = new VertexBufferObject();

            vbo.bufferData(vertexBuffer.getVertexBuffer(), BufferUsage.STREAM_DRAW);

            if (vertexBuffer.getDrawMode().doesUseIndices())
            {
                ElementBufferObject ebo = new ElementBufferObject();
                ebo.bufferData(vertexBuffer.getIndicesBuffer(), BufferUsage.STREAM_DRAW);

                vao.prepare(vertexBuffer.getVertexFormat(), vbo, ebo);
                
                return new VaoRenderableWithIndices(vao, vertexBuffer);
            }
            else
            {
                vao.prepare(vertexBuffer.getVertexFormat(), vbo);
                
                return new VaoRenderableWithoutIndices(vao, vertexBuffer);
            }
        }

        return null;
    }
}

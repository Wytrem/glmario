package net.wytrem.glmario.gl.render;

import static org.lwjgl.opengl.GL11.glDrawArrays;

import net.wytrem.glmario.gl.buffers.VertexArrayObject;
import net.wytrem.glmario.gl.buffers.VertexBuffer;

public class VaoRenderableWithoutIndices implements Renderable
{
    private VertexArrayObject vao;
    private int drawMode;
    private int vertexCount;

    public VaoRenderableWithoutIndices(VertexArrayObject vao, int drawMode, int vertexCount)
    {
        this.vao = vao;
        this.drawMode = drawMode;
        this.vertexCount = vertexCount;
    }

    public VaoRenderableWithoutIndices(VertexArrayObject vao, VertexBuffer vertexBuffer)
    {
        this(vao, vertexBuffer.getDrawMode().getGlConstant(), vertexBuffer.getVertexCount());
    }

    @Override
    public void render()
    {
        vao.bind();
        {
            glDrawArrays(drawMode, 0, vertexCount);
        }
        VertexArrayObject.bindNone();
    }
}

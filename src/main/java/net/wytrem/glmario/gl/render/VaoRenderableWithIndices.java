package net.wytrem.glmario.gl.render;

import static org.lwjgl.opengl.GL11.glDrawElements;

import net.wytrem.glmario.gl.buffers.VertexArrayObject;
import net.wytrem.glmario.gl.buffers.VertexBuffer;

public class VaoRenderableWithIndices implements Renderable
{
    private VertexArrayObject vao;
    private int drawMode;
    private int indicesCount;
    private int indicesPrimitiveType;
    
    public VaoRenderableWithIndices(VertexArrayObject vao, VertexBuffer buffer)
    {
        this(vao, buffer.getDrawMode().getGlConstant(), buffer.getIndicesCount(), buffer.getIndicesType().getGlConstant());
    }

    public VaoRenderableWithIndices(VertexArrayObject vao, int drawMode, int count, int primitiveType)
    {
        this.vao = vao;
        this.drawMode = drawMode;
        this.indicesCount = count;
        this.indicesPrimitiveType = primitiveType;
    }

    @Override
    public void render()
    {
        vao.bind();
        {
            glDrawElements(drawMode, indicesCount, indicesPrimitiveType, 0l);
        }
        VertexArrayObject.bindNone();
    }
}

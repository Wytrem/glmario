package net.wytrem.glmario.gl.buffers;

public class VboBufferUploader implements VertexBufferUploader
{
    private VertexBufferObject vbo;
    private BufferUsage usage;

    public VboBufferUploader(VertexBufferObject vbo, BufferUsage usage)
    {
        this.vbo = vbo;
        this.usage = usage;
    }

    @Override
    public void upload(VertexBuffer buffer)
    {
        vbo.bufferData(buffer.getVertexBuffer(), usage);
        buffer.reset();
    }
}

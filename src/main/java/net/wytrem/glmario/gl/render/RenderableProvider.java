package net.wytrem.glmario.gl.render;

import net.wytrem.glmario.gl.buffers.VertexBuffer;

public interface RenderableProvider
{
    Renderable provide(VertexBuffer buffer);
}

package net.wytrem.glmario.terrain;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import net.wytrem.glmario.gl.TextureRegion;
import net.wytrem.glmario.gl.buffers.VertexBuffer;
import net.wytrem.glmario.gl.buffers.VertexBuffer.Mode;
import net.wytrem.glmario.gl.format.VertexFormats;
import net.wytrem.glmario.gl.render.Renderable;
import net.wytrem.glmario.gl.render.VaoBuilder;
import net.wytrem.glmario.textures.GlTexture;
import tiled.core.Map;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;


public class TerrainRenderer
{
    private Map map;
    private HashMap<TileSet, GlTexture> tileSets = new HashMap<>(16);
    private HashMap<Tile, TextureRegion> tileIcons = new HashMap<>(64);
    private HashMap<GlTexture, Renderable> renderObjects = new HashMap<>(16);

    public TerrainRenderer(Map map)
    {
        this.map = map;
    }

    public void init()
    {
        for (TileSet set : map.getTileSets())
        {
            computeTileSet(set);
        }

        prepareRendering();
    }

    private void prepareRendering()
    {
        VertexBuffer vertexBuffer = new VertexBuffer(0xfffff);
        VaoBuilder vaoBuilder = new VaoBuilder();

        List<TileLayer> tileLayers = map.getLayers().stream().filter(TileLayer.class::isInstance).map(TileLayer.class::cast).collect(Collectors.toList());

        for (TileSet tileSet : tileSets.keySet())
        {
            vertexBuffer.begin(Mode.QUADS, VertexFormats.TILES);
            
            Iterator<TileLayer> iterator = tileLayers.iterator();
            
            while (iterator.hasNext())
            {
                TileLayer tileLayer = iterator.next();
                
                renderTileLayer(vertexBuffer, tileLayer, tileSet);
            }
            
            vertexBuffer.finishDrawing();
            
            renderObjects.put(tileSets.get(tileSet), vaoBuilder.provide(vertexBuffer));
        }
    }

    private void renderTileLayer(VertexBuffer vertexBuffer, TileLayer layer, TileSet tileSet)
    {
        final int tileWidth = map.getTileWidth();
        final int tileHeight = map.getTileHeight();

        for (int x = 0; x < layer.getWidth(); ++x)
        {
            for (int y = 0; y < layer.getHeight(); ++y)
            {
                final Tile tile = layer.getTileAt(x, y);

                if (tile == null)
                {
                    continue;
                }
                
                if (!tile.getTileSet().equals(tileSet))
                {
                    continue;
                }
                
                final Image image = tile.getImage();
                if (image == null)
                {
                    continue;
                }

                quad(x * tileWidth, (y + 1) * tileHeight - image.getHeight(null), 0.0f, image.getWidth(null), image.getHeight(null), tileIcons.get(tile), vertexBuffer);
            }
        }
    }

    private void quad(int x, int y, float z, int w, int h, TextureRegion region, VertexBuffer vertexBuffer)
    {
        vertexBuffer.pos(x, y, z).texCoords(region.getMinU(), region.getMinV()).endVertex();
        vertexBuffer.pos(x, y + h, z).texCoords(region.getMinU(), region.getMaxV()).endVertex();
        vertexBuffer.pos(x + w, y + h, z).texCoords(region.getMaxU(), region.getMaxV()).endVertex();
        vertexBuffer.pos(x + w, y, z).texCoords(region.getMaxU(), region.getMinV()).endVertex();
    }

    private void computeTileSet(TileSet tileSet)
    {
        int tileWidth = tileSet.getTileWidth();
        int tileHeight = tileSet.getTileHeight();
        int width = tileWidth * tileSet.getTilesPerRow();
        int height = (int) (Math.ceil(tileSet.size() / tileSet.getTilesPerRow())) * tileHeight;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        TextureRegion.Builder builder = new TextureRegion.Builder(width, height);

        int x, y;

        for (Tile tile : tileSet)
        {
            x = (tile.getId() % tileSet.getTilesPerRow()) * tileWidth;
            y = (tile.getId() / tileSet.getTilesPerRow()) * tileHeight;
            graphics.drawImage(tile.getImage(), x, y, null);
            tileIcons.put(tile, builder.build(x, y, tileWidth, tileHeight));
        }

        graphics.dispose();
        tileSets.put(tileSet, new GlTexture(image));
    }

    public void render()
    {

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        for (Entry<GlTexture, Renderable> entry : renderObjects.entrySet())
        {
            entry.getKey().bind();
            entry.getValue().render();
        }
        
    }

    public Map getMap()
    {
        return map;
    }
}

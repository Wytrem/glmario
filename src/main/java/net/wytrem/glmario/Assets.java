package net.wytrem.glmario;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;

import net.wytrem.glmario.textures.GlTexture;
import tiled.core.Map;
import tiled.io.TMXMapReader;

public class Assets
{
    public GlTexture background;
    public Map level1;
    
    public void load()
    {
        try
        {
            background = new GlTexture(Assets.class.getResourceAsStream("/images/background.png"));
            background.parameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
            
            TMXMapReader reader = new TMXMapReader();
            reader.setStreamResolver(path ->{
                if (!path.startsWith("/"))
                {
                    path = "/" + path;
                }
                
                return Assets.class.getResourceAsStream(path);
            });
            
            level1 = reader.readMap(Assets.class.getResourceAsStream("/mario1.tmx"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

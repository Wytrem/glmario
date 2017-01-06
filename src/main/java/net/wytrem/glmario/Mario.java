package net.wytrem.glmario;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;
import static net.wytrem.glmario.utils.MatrixStack.loadIdentity;
import static net.wytrem.glmario.utils.MatrixStack.matrixMode;
import static net.wytrem.glmario.utils.MatrixStack.ortho;
import static net.wytrem.glmario.utils.MatrixStack.scale;

import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import net.wytrem.glmario.gl.buffers.VertexBuffer;
import net.wytrem.glmario.gl.format.VertexFormats;
import net.wytrem.glmario.gl.render.Renderable;
import net.wytrem.glmario.gl.render.VaoBuilder;
import net.wytrem.glmario.gl.shaders.ShaderProgram;
import net.wytrem.glmario.terrain.TerrainRenderer;
import net.wytrem.glmario.utils.IOUtils;
import net.wytrem.glmario.utils.MatrixStack.Mode;


public class Mario
{
    static final int WIDTH = 760;
    static final int HEIGHT = 512;

    // The window handle
    private long window;

    public void run()
    {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        try
        {
            init();
            loop();

            // Free the window callbacks and destroy the window
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        }
        finally
        {
            // Terminate GLFW and free the error callback
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void init()
    {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "GlMario", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
        
    }
    
    Matrix4f projection, model, view;
    Assets assets;

    private void loop()
    {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        assets = new Assets();
        assets.load();
        
        glViewport(0, 0, WIDTH, HEIGHT);
        
        VaoBuilder builder = new VaoBuilder();
        
        VertexBuffer vertexBuffer = new VertexBuffer(4096);
        
        vertexBuffer.begin(VertexBuffer.Mode.QUADS, VertexFormats.BASE);
        vertexBuffer.pos(0.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).texCoords(0.0f, 0.0f).endVertex();
        vertexBuffer.pos(0.0f, HEIGHT).color(1.0f, 1.0f, 0.0f, 1.0f).texCoords(0.0f, 1.0f).endVertex();
        vertexBuffer.pos(WIDTH, HEIGHT).color(1.0f, 0.0f, 1.0f, 1.0f).texCoords(1.0f, 1.0f).endVertex();
        vertexBuffer.pos(WIDTH, 0.0f).color(0.0f, 1.0f, 1.0f, 1.0f).texCoords(1.0f, 0.0f).endVertex();
        vertexBuffer.finishDrawing();
        
        Renderable renderable = builder.provide(vertexBuffer);
        
        matrixMode(Mode.PROJECTION);
        loadIdentity();
        ortho(0, WIDTH, HEIGHT, 0, 1.0f, -1.0f);
        matrixMode(Mode.MODELVIEW);
        scale(2, 2);
        
        ShaderProgram basicProgram = ShaderProgram.create(IOUtils.readUTF8(Mario.class.getResourceAsStream("/shaders/base.vert")), IOUtils.readUTF8(Mario.class.getResourceAsStream("/shaders/base.frag")));

        glEnable(GL_TEXTURE_2D);
        
        TerrainRenderer terrain = new TerrainRenderer(assets.level1);
        terrain.init();
        
        ShaderProgram tilesProgram = ShaderProgram.create(IOUtils.readUTF8(Mario.class.getResourceAsStream("/shaders/tiles.vert")), IOUtils.readUTF8(Mario.class.getResourceAsStream("/shaders/tiles.frag")));
        
        
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window))
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            tilesProgram.use();
            
//            renderable.render();
            
            terrain.render();
            
            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
    
    public static void main(String[] args)
    {
        new Mario().run();
    }

}
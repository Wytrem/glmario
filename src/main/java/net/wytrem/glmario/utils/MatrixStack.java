package net.wytrem.glmario.utils;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.FloatBuffer;
import java.util.Stack;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import net.wytrem.glmario.gl.shaders.ShaderProgram;


public class MatrixStack
{
    private static final Stack<Matrix4f> projection = new Stack<>();
    private static final Stack<Matrix4f> modelView = new Stack<>();
    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

    private static Mode mode = Mode.PROJECTION;
    private static ShaderProgram current = null;

    public static final String PROJECTION_UNIFORM = "projection";
    public static final String MODELVIEW_UNIFORM = "modelView";

    static
    {
        projection.push(new Matrix4f());
        modelView.push(new Matrix4f());
    }

    public static enum Mode
    {
        PROJECTION,
        MODELVIEW;
    }

    public static void changeProgram(ShaderProgram program)
    {
        current = program;
        apply();
    }

    public static void apply()
    {
        if (current == null)
        {
            return;
        }

        projection.peek().get(buffer);
        glUniformMatrix4fv(current.getUniformLocation(PROJECTION_UNIFORM), false, buffer);
        buffer.rewind();

        modelView.peek().get(buffer);
        glUniformMatrix4fv(current.getUniformLocation(MODELVIEW_UNIFORM), false, buffer);
        buffer.rewind();
    }

    public static void ortho(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        stack().peek().ortho(left, right, bottom, top, zNear, zFar);

        apply();
    }

    public static void multMatrix(Matrix4f matrix)
    {
        stack().peek().mul(matrix);
        apply();
    }

    public static void loadIdentity()
    {
        stack().peek().identity();

        apply();
    }

    public static void pushMatrix()
    {
        stack().push(new Matrix4f().set(stack().peek()));

        apply();
    }

    public static void popMatrix()
    {
        stack().pop();

        apply();
    }
    
    public static void scale(float x, float y)
    {
        stack().peek().scale(x, y, 0.0f);
        apply();
    }

    public static void scale(float x, float y, float z)
    {
        stack().peek().scale(x, y, z);
        apply();
    }

    public static void translate(float x, float y)
    {
        stack().peek().translate(x, y, 0.0f);
        apply();
    }

    public static void translate(float x, float y, float z)
    {
        stack().peek().translate(x, y, z);
        apply();
    }

    private static Stack<Matrix4f> stack()
    {
        if (mode == Mode.PROJECTION)
        {
            return projection;
        }
        else
        {
            return modelView;
        }
    }

    public static void matrixMode(Mode newMode)
    {
        mode = newMode;
    }
}

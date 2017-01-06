package net.wytrem.glmario.gl;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_SHORT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;

public enum PrimitiveType
{
    FLOAT(Float.BYTES, "Float", GL_FLOAT),
    UBYTE(Byte.BYTES, "Unsigned Byte", GL_UNSIGNED_BYTE),
    BYTE(Byte.BYTES, "Byte", GL_BYTE),
    USHORT(Short.BYTES, "Unsigned Short", GL_UNSIGNED_SHORT),
    SHORT(Short.BYTES, "Short", GL_SHORT),
    UINT(Integer.BYTES, "Unsigned Int", GL_UNSIGNED_INT),
    INT(Integer.BYTES, "Int", GL_INT);

    private final int size;
    private final String displayName;
    private final int glConstant;

    private PrimitiveType(int sizeIn, String displayNameIn, int glConstantIn)
    {
        this.size = sizeIn;
        this.displayName = displayNameIn;
        this.glConstant = glConstantIn;
    }

    public int getSize()
    {
        return this.size;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public int getGlConstant()
    {
        return this.glConstant;
    }
}
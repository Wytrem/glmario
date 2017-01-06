package net.wytrem.glmario.gl.format;

import net.wytrem.glmario.gl.PrimitiveType;

public class VertexFormatElement
{
    private final PrimitiveType type;
    private final Usage usage;
    private final int elementCount;

    public VertexFormatElement(PrimitiveType type, Usage usage, int count)
    {
        this.type = type;
        this.usage = usage;
        this.elementCount = count;
    }

    public final PrimitiveType getType()
    {
        return this.type;
    }

    public final Usage getUsage()
    {
        return this.usage;
    }

    public final int getElementCount()
    {
        return this.elementCount;
    }

    public String toString()
    {
        return this.elementCount + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
    }

    public final int getSize()
    {
        return this.type.getSize() * this.elementCount;
    }

    public final boolean isPositionElement()
    {
        return this.usage == Usage.POSITION;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + elementCount;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((usage == null) ? 0 : usage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VertexFormatElement other = (VertexFormatElement) obj;
        if (elementCount != other.elementCount)
            return false;
        if (type != other.type)
            return false;
        if (usage != other.usage)
            return false;
        return true;
    }

    public static enum Usage
    {
        POSITION("Position"),
        NORMAL("Normal"),
        COLOR("Vertex Color"),
        TEX_COORDS("Tex Coords"),
        MATRIX("Bone Matrix"),
        BLEND_WEIGHT("Blend Weight"),
        PADDING("Padding"),
        OTHER("Other");

        private final String displayName;

        private Usage(String displayNameIn)
        {
            this.displayName = displayNameIn;
        }

        public String getDisplayName()
        {
            return this.displayName;
        }
    }
}

package net.wytrem.glmario.gl.shaders;

public class ShaderCompileException extends RuntimeException
{
    private static final long serialVersionUID = -6309728058019690755L;

    public ShaderCompileException()
    {
        super();
    }

    public ShaderCompileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ShaderCompileException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ShaderCompileException(String message)
    {
        super(message);
    }

    public ShaderCompileException(Throwable cause)
    {
        super(cause);
    }
}

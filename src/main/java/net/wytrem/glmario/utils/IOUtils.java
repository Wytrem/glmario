package net.wytrem.glmario.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


public final class IOUtils
{
    public static OutputStream copy(InputStream is, OutputStream os) throws IOException
    {
        int i = 0;
        byte[] buffer = new byte[65565];
        while ((i = is.read(buffer, 0, buffer.length)) != -1)
        {
            os.write(buffer, 0, i);
        }
        return os;
    }

    public static OutputStream copy(InputStream in, String output) throws FileNotFoundException, IOException
    {
        return copy(in, new BufferedOutputStream(new FileOutputStream(output)));
    }

    public static String readString(InputStream in, String charset)
    {
        try
        {
            return new String(read(in), charset);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

    public static byte[] read(InputStream in)
    {
        byte[] buffer = new byte[65565];
        ByteArrayOutputStream ous = new ByteArrayOutputStream(buffer.length);
        int i = 0;
        try
        {
            while ((i = in.read(buffer, 0, buffer.length)) != -1)
            {
                ous.write(buffer, 0, i);
            }

            ous.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return ous.toByteArray();
    }

    public static void deleteFolderContents(File folder)
    {
        File[] files = folder.listFiles();
        if (files != null)
            for (File f : files)
            {
                if (f.isDirectory())
                {
                    deleteFolderContents(f);
                    f.delete();
                }
                else
                    f.delete();
            }
    }

    public static String readUTF8(InputStream stream)
    {
        return readString(stream, "UTF-8");
    }
}

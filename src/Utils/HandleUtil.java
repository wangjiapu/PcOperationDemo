package Utils;

import com.google.gson.Gson;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public final class HandleUtil {
    public static final byte[] SINGLEBYTE=new byte[1];
    public static final byte[] DOUBLEBYTE=new byte[2];
    public static final Gson gson=new Gson();


    /**
     * read data from inputStream
     * @param stream:socket inputStream
     * @param dataSize:
     * @return change bytes to String
     * @throws IOException
     */
    public static String read(InputStream stream, short dataSize) throws IOException {
        int i = 0;
        byte[] dataBytes = new byte[dataSize];
        while (i < dataSize) {
            dataBytes[i] = (byte) stream.read();
            i++;
        }
        return new String(dataBytes);
    }


    /**
     * splicing return the data
     * @param b  flag is used to choose
     * @param s  data
     * @return
     */
    public static byte[] splicing(byte b,String s){

        try {
            byte[] sb = s.getBytes("UTF-8");
            byte[] sblen=IntConvertUtils.getShortBytes((short) sb.length);

            byte[] newbyte=new byte[1+sb.length+sblen.length];
            newbyte[0]=b;
            System.arraycopy(sblen,0,newbyte,1,sblen.length);
            System.arraycopy(sb,0,newbyte,sblen.length,sb.length);
            return newbyte;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] errorbyte=new byte[3];
        errorbyte[0]=b;
        errorbyte[1]=0x00;
        errorbyte[2]=0x00;
       return errorbyte;
    }



    public static byte[] splicing(byte b,byte[] bytes){
        byte[] sblen=IntConvertUtils.getShortBytes((short) bytes.length);
        byte[] newbytes=new byte[1+sblen.length+bytes.length];
        newbytes[0]=b;
        System.arraycopy(sblen,0,newbytes,1,sblen.length);
        System.arraycopy(bytes,0,newbytes,sblen.length,bytes.length);
        return newbytes;

    }


}

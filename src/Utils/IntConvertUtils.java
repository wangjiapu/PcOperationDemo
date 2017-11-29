package Utils;

public class IntConvertUtils {
    public static byte[] getShortBytes(short data){
        byte[] s = {(byte)0xff,(byte)0xff};
        s[1] = (byte)((data)&s[1]);
        data = (short)(data>>>8);
        s[0] = (byte)((data)&s[0]);
        return s;
    }

    public static short getShortByByteArray(byte[] bytes){
        short s = 0;
        s = (short)(s|bytes[0]);
        s = (short)(s << 8);
        s = (short)(s|bytes[1]);
        return s;
    }
}

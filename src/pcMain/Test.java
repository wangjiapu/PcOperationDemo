package pcMain;

import beans.DiskInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
        for (byte b : getIntegerBytes(0xffffffff)) {
            System.out.println(b);
        }
        System.out.println(getIntegerByByteArray(getIntegerBytes(355555)));
    }

    public static byte[] getIntegerBytes(int data){
        byte[] s = {(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
        s[3] = (byte)((data)&s[3]);
        for(int i = 2;i>=0;i--){
            data = data>>8;
            s[i] = (byte)((data)&s[i]);
        }
        return s;
    }

    public static int getIntegerByByteArray(byte[] data){
        int s = 0;
        s = (s|(data[0]&0xff));
        for (int i = 1; i <data.length; i++) {
            s = s<<8;
            s = s|(data[i]&0xff);
        }
        return s;
    }
}

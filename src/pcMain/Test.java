package pcMain;

import beans.DiskInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.SourceTree;
import pcOp.PcDisk;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
        PcDisk pcDisk=new PcDisk();
        Gson gson=new Gson();

        String s=gson.toJson(pcDisk.getDisk());
        System.out.println(s);
    }

}

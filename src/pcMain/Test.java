package pcMain;

import beans.DiskInfo;
import beans.FileInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.SourceTree;
import pcOp.PcDisk;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
       String s="fjkshdf_lk;smfd_mjfsdklm_mflkdsm_mflkdsm";
        System.out.println(s.substring(0,s.indexOf("_")));
        System.out.println(s.substring(s.indexOf("_")+1,s.lastIndexOf("_")));
        System.out.println(s.substring(s.lastIndexOf("_")+1,s.length()));

    }



}

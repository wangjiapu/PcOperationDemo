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
        FileSystemView fileSystemView=FileSystemView.getFileSystemView();
        for (File f:File.listRoots()){
            System.out.println(f.getPath()+"\\");
        }
    }



}

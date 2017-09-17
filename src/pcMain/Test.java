package pcMain;

import beans.DiskInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pcOp.PcMouse;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
       /* List<DiskInfo> list=new ArrayList<>();
        for(int i=0;i<5;i++){
          DiskInfo d=new DiskInfo();
          d.setDrive(i+"");
          d.setUseInfo(i+i+i+"");
          list.add(d);
        }

        Gson gson=new Gson();
        String s=gson.toJson(list);
        System.out.println(s);
        List<DiskInfo> retList = gson.fromJson(s,
                new TypeToken<List<DiskInfo>>() {
                }.getType());
        for (DiskInfo stu : retList) {
            System.out.println(stu);
        }*/

      FileSystemView fileSystemView=FileSystemView.getFileSystemView();
      for(File f: File.listRoots()){
          System.out.println(fileSystemView.getSystemDisplayName(f));
          System.out.println(fileSystemView.getSystemTypeDescription(f));
          System.out.println(f.getTotalSpace());
          System.out.println(f.getFreeSpace());
      }


    }
}

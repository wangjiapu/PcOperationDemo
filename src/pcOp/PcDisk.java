package pcOp;

import beans.DiskInfo;
import beans.FileInfo;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/*public class PcDisk {

    public static void main(String[]args){
        WindowsInfoUtil wiu=new WindowsInfoUtil();
        List<String> l=wiu.getDisk();
        for (String str:l){
            System.out.println(str);
        }

        System.out.println(wiu.getMemeryRate());
        System.out.println("cpu占有率"+wiu.getCPURate());
    }
}*/

/**
 * 参数：无
 * new PcDisk 直接调用方法
 */
public class PcDisk {
    //获取各个磁盘的使用情况
    public List<DiskInfo> getDisk() {
        List<DiskInfo> diskLists = new ArrayList<>();
        FileSystemView fileSystemView=FileSystemView.getFileSystemView();
        for (File f:File.listRoots()){
            long total = f.getTotalSpace();
            long free = f.getFreeSpace();
            Double compare = (1 - free * 1.0 / total) * 100;
            DiskInfo diskInfo=new DiskInfo();
            String diskname=fileSystemView.getSystemDisplayName(f);
            if (!diskname.isEmpty()){
                diskInfo.setPath(f.getPath()+"\\");
                diskInfo.setDrive(diskname);
                diskInfo.setUseInfo(compare.intValue()+"");
                diskLists.add(diskInfo);
            }
        }
        return diskLists;
    }


    //获取内存使用情况
    public String getMemeryRate() {
        com.sun.management.OperatingSystemMXBean operatingSystemMXBean = ManagementFactory
                .getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
        //总内存
        long totalvirtualMemory = operatingSystemMXBean.getTotalPhysicalMemorySize();
        //剩余内存
        long freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize();
        Double compare = (1 - freePhysicalMemorySize * 1.0 / totalvirtualMemory) * 100;
        String temp = "内存已使用" + compare.intValue() + "%";
        return temp;
    }

    //获取cpu的使用情况
    public String getCPURate() {
       return "0%";
    }

    //获取某个磁盘下的目录
    public  List<FileInfo> getFileDirectory(String path) {
        List<FileInfo> fileInfos=new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();//文件夹下的所有文件或文件夹
        if(files == null)
            return null;
        for (int i = 0; i < files.length; i++)
        {
            FileInfo fileInfo=new FileInfo();
            fileInfo.setPath(files[i].getAbsolutePath());
            fileInfo.setName(files[i].getName());
            if (files[i].isDirectory())
                fileInfo.setType(true);
            else
                fileInfo.setType(false);
            fileInfos.add(fileInfo);
        }
        return fileInfos;
    }
}

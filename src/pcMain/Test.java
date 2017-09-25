package pcMain;

import beans.DiskInfo;
import beans.FileInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.SourceTree;
import pcOp.PcDisk;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Test {

    public static void main(String[]args){
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Point point=MouseInfo.getPointerInfo().getLocation();
                System.out.println(point.x+"");
            }
        },100,100);
    }

}

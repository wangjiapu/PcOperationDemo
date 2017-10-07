package pcOp;


import beans.FileCommand;
import beans.FileDescribe;
import com.google.gson.Gson;
import pcMain.OpenService;
import pcMain.Parameter;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 参数 fn：保存路径+文件名称，ift：文件格式
 *使用：PcScreen p=new PcScreen(fn,ift)
 *       p.shot(); 返回true 则为成功
 */
public class PcScreen{
    private String fileName;
    //private String imageFormat;
    private String defaultFormat="jpg";
    private Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();

    public PcScreen(String fn){
        this.fileName=fn;
    }

    /**
     * 截取屏幕
     * @return 是否成功
     */
    public boolean shot(){
        try {
            BufferedImage bufferedImage=(new Robot()).createScreenCapture(
                    new Rectangle(0,0,(int)dimension.getWidth(),(int)dimension.getHeight()));
            String fName=fileName+"."+defaultFormat;
            File file=new File(fName);
            ImageIO.write(bufferedImage,defaultFormat,file);
           return true;
        } catch (AWTException e) {
            System.out.println("截屏失败");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("文件写入失败");
            e.printStackTrace();
        }
        return false;
    }

    /**
     *        将截取的屏幕回传
     * @param fn           保存路径+文件名称
     * */
    public void sendScreen(String fn ){

        String fName=fn+"."+defaultFormat;
        File file=new File(fName);
        List<File> list=new ArrayList<>();
        if (file.exists()) {
            byte[] bytes=new byte[4096];
            list.add(file);
            OpenService.sendMsg(Parameter.FILE_LIST_FLAG + "_" + getFileDescribeArray(list)+"_"+Parameter.END_FLAG);
            System.out.println(getFileDescribeArray(list));
            String s = OpenService.readString();
            if (s.startsWith(Parameter.FILE_READY)) {
                System.out.println("开始发送");
                //开始发
                list.forEach(file1 -> {
                    try {
                        FileInputStream fileInputStream=new FileInputStream(file1);
                        int count=0;
                        while (true){
                            count=fileInputStream.read(bytes);
                            if (count==-1)
                                break;
                            if (count==4096)
                                OpenService.sendBytes(bytes);
                                //OpenService.sendMsg(new String(bytes));
                            else{
                                byte[] newbytes=new byte[count];
                                for(int i=0;i<count;i++){
                                    newbytes[i]=bytes[i];
                                }
                                OpenService.sendBytes(newbytes);
                               //OpenService.sendMsg(new String(newbytes));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private String getFileDescribeArray(List<File> fileList){
        FileCommand command=new FileCommand();
        List<FileDescribe> data=new ArrayList<>();
        for (File f:fileList){
            String name=f.getName().substring(0,f.getName().lastIndexOf("."));
            String type=f.getName().substring(f.getName().lastIndexOf(".")+1,f.getName().length());
            data.add(new FileDescribe(name,type,f.length()));
        }
        int size=data.size();
        command.setDescribe((data.toArray(new FileDescribe[size])));
        command.setBack(false);
        command.setType("21");
        String s=new Gson().toJson(command);
        return s;
    }
}
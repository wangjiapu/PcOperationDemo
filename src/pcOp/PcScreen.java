package pcOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 参数 fn：保存路径+文件名称，ift：文件格式
 *使用：PcScreen p=new PcScreen(fn,ift)
 *       p.shot(); 返回true 则为成功
 */
public class PcScreen{
    private String fileName;
    //private String imageFormat;
    private String defaultFormat="png";
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
     * @param writer       socket输出流
     * @return             成功返回true 失败返回false
     */
    public boolean sendScreen(String fn,OutputStream writer){
        boolean sendOK=false;
        String fName=fn+"."+defaultFormat;
        File file=new File(fName);
        if (file.exists()){
            try {
                FileInputStream fileInputStream=new FileInputStream(file);
                System.out.println("======== 开始传输截屏 ========");
                byte[] bytes=new byte[1024];
                int length=0;
                long progress=0;
                String temp;
                while((length=fileInputStream.read(bytes))!=-1){
                    writer.write(bytes,0,length);
                    System.out.println(progress);
                    progress+=length;
                }
                System.out.println("======== 传输截屏成功 ========");
                sendOK=true;
            } catch (FileNotFoundException e) {
                System.out.println("文件读取失败");
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }else{
            System.out.println("文件"+fName+"不存在！");
        }
        return sendOK;
    }
}
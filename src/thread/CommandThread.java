package thread;

import Utils.HandleUtil;
import Utils.IntConvertUtils;
import beans.*;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import pcOp.PcDisk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Handles commands that need to be returned
 */
public class CommandThread extends Thread{

    private static final String TAG="CommandThread";
    private String filename;
    private static boolean flag=false;

    private  static Queue<PackByteArray> cmdQueue=new LinkedBlockingDeque<>(1024);

    private  OutputThread out=OutputThread.INSTANCE;


    private CommandThread(){ }

    public static CommandThread getInstance(){
        return ClassCommand.INSTANCE;
    }
    private static class ClassCommand{
        private static CommandThread INSTANCE=new CommandThread();
    }


    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                handle();
            }
        }catch (Exception e){
            System.out.println(TAG+e.getMessage());
        }
    }

    private void handle() throws IOException {

       PackByteArray pba=cmdQueue.peek();
        byte type=pba.getFlag();
        switch (type){
            case 0x21:

                short  dataSize= pba.getLen();
                if (dataSize>0 && pba.getBody()!=null){
                    String data= new String(pba.getBody());
                    Command command=HandleUtil.gson.fromJson(data,Command.class);
                    commandSwitch(command);
                    return;
                }
                break;
            case 0x22:
                break;
        }

    }

    private void commandSwitch(Command command) throws IOException {
        int type = Integer.valueOf(command.getType());
        switch (type) {
            case 2:
                filename=command.getDescribe();
                sendScreeninfo(filename);
                break;
            case 4://获取磁盘详细信息
                PcDisk pcDisk = new PcDisk();
                if (command.getDescribe().isEmpty()) {
                    String diskinfo=HandleUtil.gson.toJson(pcDisk.getDisk());
                    byte[] bytes=diskinfo.getBytes("UTF-8");
                    PackByteArray pack=new PackByteArray(ProtocolField.command,
                            IntConvertUtils.getShortByByteArray(bytes),bytes);
                    out.addMessageHighLevel(pack);
                } else {

                    String dir = HandleUtil.gson.toJson(pcDisk
                            .getFileDirectory(command.getDescribe()));
                    byte[] bytes=dir.getBytes();
                    PackByteArray pack=new PackByteArray(ProtocolField.command,
                            IntConvertUtils.getShortByByteArray(bytes),bytes);
                    out.addMessageHighLevel(pack);
                }
                break;
            default:
                sendScreen(command.getDescribe());

        }
    }

    private void sendScreen(String name) throws IOException {
        if (flag && name.equals(filename)){
            System.out.println("开始发送");
            //开始发
            byte[] bytes=new byte[4096];
            FileInputStream fileInputStream=new FileInputStream(filename+".jpg");
            int count=0;
            while (true){
                count=fileInputStream.read(bytes);
                if (count==-1)
                    break;
                if (count==4096){
                    PackByteArray pack=new PackByteArray(ProtocolField.cmdScreenBody,
                            IntConvertUtils.getShortByByteArray(bytes),bytes);
                    out.addMessageHighLevel(pack);
                }
                else{
                    byte[] newbytes=new byte[count];
                    for(int i=0;i<count;i++){
                        newbytes[i]=bytes[i];
                    }
                    PackByteArray pack=new PackByteArray(ProtocolField.cmdScreenBody,
                            IntConvertUtils.getShortByByteArray(newbytes),newbytes);
                    out.addMessageHighLevel(pack);
                    flag=false;
                    filename=null;
                }
            }

        }
    }

    /**
     *        将截取的屏幕回传
     * */
    private void sendScreeninfo(String describe) throws IOException {

        String fName=describe+".jpg";
        File file=new File(fName);
        if (file.exists()){
            String screeninfo=getScreeninfo(file);
            byte[] data=screeninfo.getBytes();
            PackByteArray pack=new PackByteArray(ProtocolField.commandreturn,
                    IntConvertUtils.getShortByByteArray(data),data);
            out.addMessageHighLevel(pack);
            flag=true;
        }
    }

    private String getScreeninfo(File f) {
        FileCommand command=new FileCommand();
        List<FileDescribe> data=new ArrayList<>();
        String name=f.getName().substring(0,f.getName().lastIndexOf("."));
        String type=f.getName().substring(f.getName().lastIndexOf(".")+1,f.getName().length());
        data.add(new FileDescribe(name,type,f.length()));
        int size=data.size();
        command.setDescribe((data.toArray(new FileDescribe[size])));
        command.setType("21");
        String s=new Gson().toJson(command);
        return s;
    }



    public void addMessage(@NotNull PackByteArray byteArray) {
        cmdQueue.add(byteArray);
    }

}

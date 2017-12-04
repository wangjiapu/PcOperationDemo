package thread;

import Utils.HandleUtil;
import Utils.IntConvertUtils;
import beans.Command;
import beans.FileCommand;
import beans.FileDescribe;
import beans.ProtocolField;
import com.google.gson.Gson;
import pcOp.PcDisk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles commands that need to be returned
 */
public class CommandThread extends Thread {

    private String filename;
    private static boolean flag=false;
    private  PipedOutputStream mPos;
    private  PipedInputStream mPis;


    public CommandThread(PipedInputStream pis, PipedOutputStream pos){
        this.mPis=pis;
        this.mPos=pos;
    }

    @Override
    public void run() {
        while (true){
            try {
                handle(mPis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handle(InputStream stream) throws IOException {

        int size=stream.read(HandleUtil.SINGLEBYTE);
        System.out.println("dispach,singleByte,size:"+size);
        byte type=HandleUtil.SINGLEBYTE[0];
        switch (type){
            case 0x21:
                int cmdSize = 0;
                try {
                    cmdSize = stream.read(HandleUtil.DOUBLEBYTE);
                    System.out.println("need return command size:"+cmdSize);
                    short  dataSize= IntConvertUtils.getShortByByteArray(HandleUtil.DOUBLEBYTE);
                    if (dataSize<=0){
                        return;
                    }
                    String data= HandleUtil.read(stream,dataSize);
                    Command command=HandleUtil.gson.fromJson(data,Command.class);
                    commandSwitch(command);
                } catch (IOException e) {
                    e.printStackTrace();
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

                    mPos.write(HandleUtil.splicing(ProtocolField.command,diskinfo));
                    mPos.flush();
                } else {

                    String dir = HandleUtil.gson.toJson(pcDisk
                            .getFileDirectory(command.getDescribe()));
                    mPos.write(HandleUtil.splicing(ProtocolField.command,dir));
                    mPos.flush();
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
            byte[] bytes=new byte[1020];
            FileInputStream fileInputStream=new FileInputStream(filename+".jpg");
            int count=0;
            while (true){
                count=fileInputStream.read(bytes);
                if (count==-1)
                    break;
                if (count==1020)
                    mPos.write(HandleUtil.splicing(ProtocolField.cmdScreenBody,bytes));
                else{
                    byte[] newbytes=new byte[count];
                    for(int i=0;i<count;i++){
                        newbytes[i]=bytes[i];
                    }
                    mPos.write(HandleUtil.splicing(ProtocolField.cmdScreenBody,newbytes));
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
            byte[] data=HandleUtil.splicing(ProtocolField.commandreturn,screeninfo);
            mPos.write(data);
            mPos.flush();
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

}

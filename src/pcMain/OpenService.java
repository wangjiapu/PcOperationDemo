package pcMain;

import Utils.IntConvertUtils;
import beans.Command;
import beans.DiskInfo;
import beans.FileCommand;
import beans.FileDescribe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sun.istack.internal.NotNull;
import pcOp.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class OpenService {
    private static Socket connSocket;
    private static OutputStream os;
    private static InputStream is;
    private static BufferedReader reader;
    private static Gson gson=new Gson();
    private static boolean loopFlag = true;

    public static void main(String[] args) {

        try {
            connSocket = new Socket(Parameter.SERVER_IP, 10087);
            OutputStream outputStream = connSocket.getOutputStream();
            InputStream inputStream = connSocket.getInputStream();

            os=connSocket.getOutputStream();
            is = connSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            //向服务端发送消息，我要上线
            String username = System.getProperty("user.name");
            System.out.println(username);
            /*writer.println("|ONLINE|_" + "lzl471954654" + "_Test_" + Parameter.END_FLAG);
            writer.flush();*/
            sendMsg("|ONLINE|_" + "lzl471954654" + "_Test_" + Parameter.END_FLAG);
            String result = readString();
            System.out.println(result);
            if (StringUtils.startAndEnd(result)) {

                /**
                 * 下线
                 * 时间限制
                 * 离线
                 */

                loop();
            } else {  //上线失败
                System.out.println("上线失败，请重新上线。。。");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void loop() {
        while (loopFlag) {
            try{
                //持续不断的读取服务端消息
                String op = readString();
                if(!op.endsWith(Parameter.END_FLAG))
                    continue;
                System.out.println("op is :"+op);
                String[] index=op.split("_");
                System.out.println("op length is "+index.length);
                if(index.length<2)
                    continue;
                if(index[0].equals(Parameter.FILE_LIST_FLAG)){
                    FileCommand command = gson.fromJson(index[1],FileCommand.class);
                    fileOperation(command,index[1]);
                }
                Command command=gson.fromJson(index[1], Command.class);
                operation(command);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private static void sendMsg(@NotNull String s){
        try {
            byte[] bytes = s.getBytes("UTF-8");
            os.write(IntConvertUtils.getIntegerBytes(bytes.length));
            os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readString(){
        String s = "";
        try {
            int msgSize = 0;
            byte[] msgSizeBytes = new byte[4];
            int readSize = is.read(msgSizeBytes);
            System.out.println("readSIze is "+readSize);
            msgSize = IntConvertUtils.getIntegerByByteArray(msgSizeBytes);
            if(msgSize<=0)
            {
                loopFlag = false;
                return "";
            }
            System.out.println("msgSize is "+msgSize);

            int i = 0;
            byte[] dataBytes = new byte[msgSize];
            while(i<msgSize){
                dataBytes[i] = (byte)is.read();
                i++;
            }
            s = new String(dataBytes);
            System.out.println("msg is "+s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 对电脑的操作
     */
    private static void operation(Command command) {
        int type=Integer.valueOf(command.getType());
        switch (type) {
            case -1:
                send2service();
                break;
            case 0://关机
                PcTools pcTools = new PcTools();
                pcTools.opencmd(21);
               // send2service(writer);
                break;
            case 1://取消关机
                PcTools pcTools1=new PcTools();
                pcTools1.opencmd(23);
                break;

            case 2:
                PcScreen pcScreen=new PcScreen(command.getDescribe());
                if (command.getIsBack()){
                    pcScreen.sendScreen(command.getDescribe(), os);
                }else {
                    pcScreen.shot();
                }
                break;

            case 4://获取磁盘详细信息
                PcDisk pcDisk=new PcDisk();
                String diskString=gson.toJson(pcDisk.getDisk());
                sendMsg(diskString);
                break;
            case 7:
                PcSearch pcSearch=new PcSearch();
                pcSearch.voiceSearch(command.getDescribe());
                break;
            default:
                PcTools tool=new PcTools();
                if (type<127){
                    tool.opencmd(type%100);
                }else{
                    tool.closecmd(type%200);
                }

        }
    }

    private static void fileOperation(FileCommand command,String jsonSrc){
        int type = Integer.valueOf(command.getType());
        switch (type){
            case 20:
                acceptFile(command.getDescribe(),jsonSrc);
                break;
        }
    }

    private static void acceptFile(FileDescribe[] describes,String jsonSrc){
        sendMsg(Parameter.FILE_READY+"_"+jsonSrc+"_"+Parameter.END_FLAG);
        for (FileDescribe describe : describes) {
            String fileName = describe.getFileName()+"."+describe.getFileType();
            Long fileSize = describe.getFileSize();
            int count = 0;
            long size = 0;
            File file = new File(fileName);
            FileOutputStream outputStream = null;
            BufferedInputStream inputStream = null;

            try{
                byte[] bytes = new byte[4096];
                outputStream = new FileOutputStream(file);
                inputStream = new BufferedInputStream(connSocket.getInputStream());
                while((count = inputStream.read(bytes))!=-1){
                    System.out.println("count is "+count);
                    outputStream.write(bytes,0,count);
                    outputStream.flush();
                    size+=count;
                    if(size>=fileSize)
                        break;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            finally {
                try{
                    if(outputStream!=null)
                        outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


    private static void newVisionSend(@NotNull String data){
        try {
            byte[] bytes = data.getBytes("UTF-8");
            connSocket.getOutputStream().write(bytes);
            connSocket.getOutputStream().flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void send2service() {
       sendMsg(Parameter.OK+"_"+Parameter.END_FLAG);
    }
}

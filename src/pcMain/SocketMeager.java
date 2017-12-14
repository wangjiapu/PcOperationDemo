package pcMain;

import Utils.DataUtil;
import Utils.IntConvertUtils;
import beans.*;
import thread.InputThread;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 连接服务端socket,
 * 断开服务端socket
 */
public  class SocketMeager extends Thread {
    private static boolean loopFlag = true;

    private InputStream is;
    private OutputStream os;

    private static InputThread out=new InputThread();
  //  private static Socket mSocket;
    private static Queue<PackByteArray> socketQueue=new LinkedBlockingDeque<>(1024);

    public SocketMeager(){ }

    @Override
    public void run() {
        while (true){
            startSocket("pjw","1111");
            try {
                loopFlag=true;
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void startSocket(String username, String pwd){

        try {
            Socket mSocket = new Socket(Parameter.SERVER_IP, 10087);
            /**
             * 处理1， 登录
             */
            os=mSocket.getOutputStream();
            is=mSocket.getInputStream();

            os.write(ProtocolField.pcOnline);
            byte[] bytes = (username+"|"+pwd).getBytes("UTF-8");
            os.write(bytes.length);
            os.write(bytes);
            os.flush();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int size=is.read(bytes);
            System.out.println("server return size;"+size);
            if (DataUtil.getType(bytes[0])==1) {
                loopFlag = true;
                loop();
            } else {  //上线失败
                loopFlag=false;
                System.out.println("Fail online, please relive。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理2. 登录成功后转发
     */
    private   void loop() {
        while (loopFlag){
            try {

                byte[] bytes=new byte[3];
                int size=is.read(bytes);
               // System.out.println("pipe1,size:"+size);
                byte[] sb={bytes[1],bytes[2]};
                short datasize= IntConvertUtils.getShortByByteArray(sb);
                if (datasize>0){
                    byte[] data=new byte[datasize];
                    int realsize=is.read(data);
                   // System.out.println("realSize,"+realsize);
                    PackByteArray pack=new PackByteArray(bytes[0],datasize,data);
                    out.addMessage(pack);
                }else{
                    PackByteArray pack=new PackByteArray(bytes[0],datasize,null);
                    out.addMessage(pack);
                }
                if (!socketQueue.isEmpty()){
                    PackByteArray pba=socketQueue.peek();
                    os.write(pba.getFlag());
                    os.write(pba.getLen());
                    if (pba.getBody()!=null){
                        os.write(pba.getBody());
                    }
                    os.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMessage(PackByteArray packByteArray){
            socketQueue.add(packByteArray);
    }
}

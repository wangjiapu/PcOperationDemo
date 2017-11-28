package pcMain;

import Utils.DataUtil;
import beans.Command;
import beans.Content;
import beans.FileCommand;
import beans.ProtocolField;
import thread.InputThread;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;

/**
 * 连接服务端socket,
 * 断开服务端socket
 */
public  class SocketMeager extends Thread {
    private static boolean loopFlag = true;
    private PipedOutputStream out;//做生产者时
    private PipedInputStream pis;//做消费者

    private InputStream is;
    private OutputStream os;

    private static Socket mSocket;

    SocketMeager(PipedOutputStream pout,PipedInputStream is){
        this.pis=is;
        this.out=pout;
    }

    @Override
    public void run() {
        while (true){
            startSocket("ppp","1111");
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
            mSocket = new Socket(Parameter.SERVER_IP, 10087);
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

            is.read(bytes);
            if (DataUtil.getType(bytes[0])==1) {
                loopFlag = true;
                loop();
            } else {  //上线失败
                loopFlag=false;
                System.out.println("上线失败，请重新上线。。。");
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
                byte[] bytes=new byte[1024];
                is.read(bytes);
                out.write(bytes);
                out.flush();

                byte[] outbyte=new byte[1024];
                pis.read(outbyte);
                os.write(outbyte);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

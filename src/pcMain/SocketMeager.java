package pcMain;

import thread.InputThread;

import java.io.*;
import java.net.Socket;

/**
 * 连接服务端socket,
 * 断开服务端socket
 */
public  class SocketMeager extends Thread {
    public static boolean loopFlag = true;
    private PipedOutputStream out;//做生产者时
    private PipedInputStream pis;//做消费者

    private static Socket mSocket;

    SocketMeager(PipedOutputStream pout,PipedInputStream is){
        this.pis=is;
        this.out=pout;
    }

    @Override
    public void run() {

    }

    public  void startSocket(String username, String pwd){

        try {
            mSocket = new Socket(Parameter.SERVER_IP, 10087);
            /**
             * 处理1， 登录
             * 处理2. 登录成功后转发
             */

          // out.write();
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* try {
            InputThread inputThread=new InputThread()
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean f=false;
        try {
            mSocket = new Socket(Parameter.SERVER_IP, 10087);

           // InputStream inputStream = mSocket.getInputStream();

            //os = connSocket.getOutputStream();
           // is = connSocket.getInputStream();
           // reader = new BufferedReader(new InputStreamReader(inputStream));

           // sendMsg("|ONLINE|_" + username + "_"+pwd+"_" + Parameter.END_FLAG);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          //  String result = readString();
           // System.out.println(result);
            if (StringUtils.startAndEnd(result)) {
                loopFlag = true;
            } else {  //上线失败
                loopFlag=false;
                System.out.println("上线失败，请重新上线。。。");
            }

        } catch (IOException e) {
            loopFlag=false;
            e.printStackTrace();
        }
        return loopFlag;*/
    }


  /*  *//**
     * 获得连接对象输入流
     * @return 返回socket的输入流
     *//*
    public static  InputStream getInputStream(){
        try {
            return mSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    *//**
     * 获得连接对象输出流
     * @return 返回socket的输出流
     *//*
    public static OutputStream getOutputStream(){
        try {
            return mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private SocketMeager(){
    }
    private static class Meager{
        private static SocketMeager meager=new SocketMeager();
    }
    public static SocketMeager getToolMeager(){
        return Meager.meager;
    }

    *//**
     * 关闭已经连接的socket
     *//*
    public static void close(){

    }*/
}

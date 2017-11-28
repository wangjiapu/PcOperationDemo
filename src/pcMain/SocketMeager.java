package pcMain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 连接服务端socket,
 * 断开服务端socket
 */
public final class SocketMeager {
    private static Socket mSocket;
    static {
        try {
            mSocket=new Socket(Parameter.SERVER_IP, 10087);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得连接对象输入流
     * @return 返回socket的输入流
     */
    public static  InputStream getInputStream(){
        try {
            return mSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获得连接对象输出流
     * @return 返回socket的输出流
     */
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

    /**
     * 关闭已经连接的socket
     */
    public static void close(){

    }
}

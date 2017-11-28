package thread;

import Utils.DataUtil;
import Utils.IntConvertUtils;
import beans.Command;
import com.google.gson.Gson;
import pcMain.SocketMeager;
import pcOp.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;


/**
 *  Process the data in the inputStream
 * Implementation of different command event handling
 */
public class InputThread extends Thread{
    private static final byte[] singleByte=new byte[1];
    private static final byte[] doubleByte=new byte[2];
    private static final Gson gson=new Gson();

    private static final PipedOutputStream out=new PipedOutputStream();
    private static final PipedInputStream input=new PipedInputStream();

    private PipedInputStream pis;
    public InputThread(PipedInputStream p){
        this.pis=p;
    }
    /**
     * Shunt files and commands into different threads
     */
    private void dispach(InputStream stream) throws IOException {
        //Unified operation
        stream.read(singleByte);
        byte type=singleByte[0];
        switch (DataUtil.getType(type)){
            case -1://no dispach
                stream.read(doubleByte);
                int  dataSize=IntConvertUtils.getIntegerByByteArray(doubleByte);
                if (dataSize<=0){
                    return;
                }
                String data=read(stream,dataSize);
                Command command = gson.fromJson(data, Command.class);
                operation(command);
                break;
            case 0://登录处理

                break;
            case 1://文件
                break;
            case 2://需要返回的命令
                break;
        }
    }

    /**
     * read data from inputStream
     * @param stream:socket inputStream
     * @param dataSize:
     * @return change bytes to String
     * @throws IOException
     */
    private String read(InputStream stream, int dataSize) throws IOException {
        int i = 0;
        byte[] dataBytes = new byte[dataSize];
        while (i < dataSize) {
            dataBytes[i] = (byte) stream.read();
            i++;
        }
        return new String(dataBytes);
    }


    /**
     * Operation without return data
     *
     */
    private static void operation(Command command) {
        int type = Integer.valueOf(command.getType());
        switch (type) {
            case 0://关机
                PcTools pcTools = new PcTools();
                pcTools.opencmd(16);
                // send2service(writer);
                break;
            case 1://取消关机
                PcTools pcTools1 = new PcTools();
                pcTools1.opencmd(18);
                break;

            case 2:
                PcScreen pcScreen = new PcScreen(command.getDescribe());
                if (command.getIsBack()) {
                    pcScreen.sendScreen(command.getDescribe());
                } else {
                    pcScreen.shot();
                }
                break;

            case 5://亮度调节
                PcLuminance pcLuminance=new PcLuminance();
                pcLuminance.brightnessOperation(command.getDescribe());
                break;
            case 7:
                PcSearch pcSearch = new PcSearch();
                pcSearch.voiceSearch(command.getDescribe());
                break;
            case 8:
                PcVolume pcVolume=new PcVolume();//获取音量调节实例
                int t=Integer.valueOf(command.getDescribe());
                if (t>0){
                    for(int i=t;i>=0;i-=2){
                        pcVolume.vol_Increase();
                    }
                }else if (t<0){//循环调节音量
                    for (int i=t;i<=0;i+=2){
                        pcVolume.vol_Decrease();
                    }
                }
                break;
            default:
                PcTools tool = new PcTools();
                if (type < 127) {
                    tool.opencmd(type % 100);
                } else {
                    tool.closecmd(type % 200);
                }

        }
    }


}

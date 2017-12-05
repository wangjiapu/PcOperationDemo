package thread;

import Utils.DataUtil;
import Utils.HandleUtil;
import beans.Command;
import beans.PackByteArray;
import org.jetbrains.annotations.NotNull;
import pcOp.*;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 *  Process the data in the inputStream
 * Implementation of different command event handling
 */
public class InputThread extends Thread{

    private static final String TAG="InputThread";

    private static Queue<PackByteArray> mQueue=new LinkedBlockingDeque<>(1024);

    private static FileInputThread fileInputThread=new FileInputThread();
    private static FileOutPutThread fileOutPutThread=new FileOutPutThread();
    private static CommandThread commandThread=new CommandThread();

    public InputThread(){ }

    @Override
    public void run() {
        inputLoop();
    }

    private void inputLoop() {
        try{
            while (!Thread.interrupted()){
                dispach();
            }
        }catch (Exception e){
            System.out.println(TAG+e.getMessage());
        }

    }

    /**
     * Shunt files and commands into different threads
     */
    private void dispach() throws IOException {
        PackByteArray pba=mQueue.peek();
        byte type=pba.getFlag();
        switch (type){
            case 0x20://this command no return

                short  dataSize=pba.getLen();
                if (dataSize>0 && pba.getBody()!=null){
                    Command command = HandleUtil.gson.fromJson(new String(pba.getBody()), Command.class);
                    operation(command);
                }
                break;
            default:
                   switch (DataUtil.getType(type)){
                       case 1:
                           fileInputThread.addMessage(pba);
                           break;
                       case 2://need return
                           commandThread.addMessage(pba);
                           break;
                       default:
                           fileOutPutThread.addMessage(pba);

                   }
        }

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
                pcScreen.shot();
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


    public void addMessage(@NotNull PackByteArray byteArray) {
        mQueue.add(byteArray);
    }
}

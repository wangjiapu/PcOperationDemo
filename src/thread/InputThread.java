package thread;

import Utils.HandleUtil;
import Utils.IntConvertUtils;
import beans.Command;
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
    private PipedInputStream pis;
    private PipedOutputStream mPos2cmd;
    private PipedOutputStream mPos3file;

    public InputThread(PipedInputStream p,PipedOutputStream p2,PipedOutputStream p3){
        this.pis=p;
        this.mPos2cmd=p2;
        this.mPos3file=p3;
    }

    @Override
    public void run() {
        inputLoop();
    }

    private void inputLoop() {
        while (true){
            try {
                dispach(pis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shunt files and commands into different threads
     */
    private void dispach(InputStream stream) throws IOException {
        int size=stream.read(HandleUtil.SINGLEBYTE);
        System.out.println("dispach,singleByte,size:"+size);
        byte type=HandleUtil.SINGLEBYTE[0];
        switch (type){
            case 0x10:
            case 0x12:

            case 0x13:
            case 0x15:

            case 0x18:
            case 0x19:
            case 0x1a:
                mPos3file.write(type);
                mPos3file.flush();
                break;
            case 0x20://this command no return
                int size_1 = stream.read(HandleUtil.DOUBLEBYTE);
                System.out.println("no dispach size:"+size_1);
                short  dataSize=IntConvertUtils.getShortByByteArray(HandleUtil.DOUBLEBYTE);
                if (dataSize<=0){
                    return;
                }
                String data=HandleUtil.read(stream,dataSize);

                Command command = HandleUtil.gson.fromJson(data, Command.class);
                operation(command);
                break;
            case 0x21://need return
            case 0x22:
                int size2=stream.read(HandleUtil.DOUBLEBYTE);
                System.out.println("need return cmd size2:"+size2);
                short cmdSize=IntConvertUtils.getShortByByteArray(HandleUtil.DOUBLEBYTE);
                if (cmdSize<=0){
                    return;
                }
                byte[] cmd=new byte[cmdSize];
                int send2nextSize=stream.read(cmd);
                System.out.println("send to commandThread size:"+send2nextSize);
                mPos2cmd.write(type);
                mPos2cmd.write(cmd);
                mPos2cmd.flush();
                break;
           default:
               int size3=stream.read(HandleUtil.DOUBLEBYTE);
               System.out.println("file size:"+size3);
               short fileSize=IntConvertUtils.getShortByByteArray(HandleUtil.DOUBLEBYTE);
               if (fileSize<=0){
                   return;
               }
               byte[] file=new byte[fileSize];
               int ss=stream.read(file);
               System.out.println("file size:"+ss);
               mPos3file.write(type);
               mPos3file.write(file);
               mPos3file.flush();

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


}

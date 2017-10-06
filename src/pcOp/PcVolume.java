package pcOp;

import java.io.IOException;

/**
 * 参数：无
 * 音量操作类
 */
public class PcVolume{
    private static final String ROOTPATH="src/tools/";
    /**
     * 设置静音
     * @return
     */
    public boolean setVol_Mute(){
        String[] mute_cmd=new String[]{"wscript",ROOTPATH + "mute.vbs"};
       return run(mute_cmd)==0;
    }

    /**
     * 增大音量
     * @return
     */
    public boolean vol_Increase(){
        String[] increase_cmd=new String[]{"wscript",ROOTPATH + "increase.vbs"};
        return run(increase_cmd)==0;
    }

    /**
     * 减小音量
     * @return
     */
    public boolean vol_Decrease(){
        String[] decrease_cmd=new String[]{"wscript",ROOTPATH + "decrease.vbs"};
        return run(decrease_cmd)==0;
    }

    private int run(String [] cmd){
        int result=-1;
        try {
            Process process=Runtime.getRuntime().exec(cmd);
            result= process.waitFor();
        } catch (IOException e) {
            System.out.println("执行出错！");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

}


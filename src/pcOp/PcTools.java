package pcOp;

import java.io.IOException;

public class PcTools{

    /**
     * 0：任务管理器
     * 1：写字板
     * 2：画图
     * 3:控制台
     * 4:设备管理器
     * 5:DVD播放器
     * 6:记事本
     * 7:讲述人
     * 8:事件查看器
     * 9:资源管理器
     * 10:性能查看器
     * 11：注册表
     * 12:计算器
     * 13:SQL SERVER 客户端网络实用程序
     * 14:垃圾整理
     * 16:屏幕键盘
     * 17:ODBC数据源管理器
     * 18：注销命令
     * 19:共享文件夹管理器
     * 20:辅助工具管理器
     */
    private static final String[] cmd=new String[]{"taskmgr","write","mspaint",
   "dvdplay","notepad","narrator","explorer",
    "regedit.exe","calc","cliconfg","cleanmgr","osk","odbcad32","logoff",
            "fsmgmt.msc","utilman","shutdown -s -t 30","shutdown -r -t 30","shutdown -a"};

    private static final String[] closeCmd=new String[]{"taskmgr", "wordpad.exe","mspaint.exe",
           "setup_wm.exe","notepad.exe","Narrator.exe","explorer.exe",
            "regedit.exe","Calculator.exe","cliconfg.exe","cleanmgr.exe","osk.exe","odbcad32.exe","logoff.exe",
            "fsmgmt.msc.exe","utilman.exe"};

    private static final String CMD="cmd /c ";
    private static final String CLOSE="taskkill /im ";
    private static final String F=" /f";

    /**
     *
     * @param i
     * 表示要执行哪一个命令
     */

    public void opencmd(int i){
        Runtime runtime=Runtime.getRuntime();
        try {
            runtime.exec(CMD+cmd[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closecmd(int i){
        Runtime runtime=Runtime.getRuntime();
        try {
            runtime.exec(CMD+CLOSE+closeCmd[i]+F);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


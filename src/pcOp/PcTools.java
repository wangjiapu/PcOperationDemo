package pcOp;

import java.io.IOException;

public class PcTools{



    private static final String CMD="cmd /c ";
    private static final String CLOSE="taskkill /im "; private static final String[] cmd=new String[]{"taskmgr","write","mspaint",
            "dvdplay","notepad","narrator","explorer",
            "regedit.exe","calc","cliconfg","cleanmgr","osk","odbcad32","logoff",
            "fsmgmt.msc","utilman","shutdown -s -t 30","shutdown -r -t 30","shutdown -a"};

    private static final String[] closeCmd=new String[]{"taskmgr.exe", "wordpad.exe","mspaint.exe",
            "setup_wm.exe","notepad.exe","Narrator.exe","explorer.exe",
            "regedit.exe","Calculator.exe","cliconfg.exe","cleanmgr.exe","osk.exe","odbcad32.exe","logoff.exe",
            "fsmgmt.msc.exe","utilman.exe"};

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


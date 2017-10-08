package pcOp;


import java.io.IOException;


public class PcLuminance{
    private static final String ROOTPATH="src/tools/ClickMonitorDDC.exe brightness";

    public void brightnessOperation(String type){
        if (type.equals("-1")){
            closeB();
            return;
        }
        try {
            Runtime.getRuntime().exec(ROOTPATH + type);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }
    public void closeB(){
        try {
            Runtime.getRuntime().exec("cmd /c taskkill /im ClickMonitorDDC.exe /f");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

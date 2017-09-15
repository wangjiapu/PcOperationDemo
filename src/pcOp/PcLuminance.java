package pcOp;


import java.io.IOException;


public class PcLuminance{
    private static final String ROOTPATH="F:\\Java\\PcOperationDemo\\src\\tools\\ClickMonitorDDC.exe brightness";

    public void brightnessOperation(String type){
        try {
            System.out.println(ROOTPATH + type);
            Process process=Runtime.getRuntime().exec(ROOTPATH + type);
            System.out.println(process.waitFor());
        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

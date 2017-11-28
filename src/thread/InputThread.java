package thread;

import Utils.IntConvertUtils;

import java.io.IOException;
import java.io.InputStream;

public class InputThread extends Thread{
    @Override
    public void run() {
        //循坏读取
        super.run();
    }

    /**
     * 将文件与命令分流到不同的线程中
     */
    private void dispach(InputStream stream){
        byte[] type=new byte[4];
        try {
            stream.read(type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (IntConvertUtils.getType(type)){
            case 0://命令
                break;
            case 1://文件
                break;
        }
    }

}

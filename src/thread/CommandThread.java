package thread;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Handles commands that need to be returned
 */
public class CommandThread extends Thread {

    private  PipedOutputStream mPos;
    private  PipedInputStream mPis;

    public CommandThread(PipedInputStream pis, PipedOutputStream pos){
        this.mPis=pis;
        this.mPos=pos;
    }
}

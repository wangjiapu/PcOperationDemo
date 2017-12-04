package thread

import java.io.PipedInputStream
import java.io.PipedOutputStream

/**
 * Send data to the server
 */
class OutputThread(val pos:PipedOutputStream,val mPisfromFile:PipedInputStream,
                   val mPisfromCmd:PipedInputStream) :Thread(){



}
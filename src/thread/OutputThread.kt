package thread

import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.concurrent.LinkedBlockingDeque

/**
 * Send data to the server
 */
class OutputThread(val pos:PipedOutputStream,val mPisfromFile:PipedInputStream,
                   val mPisfromCmd:PipedInputStream) :Thread(){

    private val outQueue=LinkedBlockingDeque<ByteArray>(1024)

    override fun run() {

    }

   /* public fun addMessage(b:ByteArray){
        outQueue.
    }*/

}
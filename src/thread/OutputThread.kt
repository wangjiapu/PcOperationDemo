package thread

import java.io.PipedInputStream
import java.io.PipedOutputStream

/**
 * Send data to the server
 */
class OutputThread(var pos:PipedOutputStream) :Thread(){
}
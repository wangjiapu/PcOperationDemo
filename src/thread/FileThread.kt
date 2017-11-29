package thread

import java.io.PipedInputStream
import java.io.PipedOutputStream

class FileThread(val pis:PipedInputStream,val pos:PipedOutputStream):Thread(){

}
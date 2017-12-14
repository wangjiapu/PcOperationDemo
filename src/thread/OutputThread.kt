package thread

import beans.PackByteArray
import pcMain.SocketMeager
import java.util.concurrent.LinkedBlockingDeque

/**
 * Send data to the server
 */


object OutputThread :Thread(){

    private val TAG="OutputThread"

    private val outQueue=LinkedBlockingDeque<PackByteArray>(1024)

    private val out = SocketMeager.getInstance()


    override fun run() {
        try {
            while (!Thread.interrupted()){
                sendMessage()
            }
        }catch (e:Exception){
            println("$TAG:${e.message}")
        }
    }


    fun addMessage(byteArray: PackByteArray) {
        outQueue.putLast(byteArray)
    }

     fun sendMessage() {
        out.addMessage(outQueue.pop())
    }

     fun addMessageHighLevel(byteArray: PackByteArray) {
        outQueue.putFirst(byteArray)
    }

}
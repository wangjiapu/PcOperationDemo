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

    private val out = SocketMeager()

   //  private constructor()



    /*fun getInstance(){
        return NewClass.instance
    }

    private object NewClass{
        val instance=OutputThread()
    }

    private operator fun invoke() {}*/

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
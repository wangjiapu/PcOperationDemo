package thread

import Utils.HandleUtil
import Utils.IntConvertUtils
import beans.ProtocolField
import java.io.IOException
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

class FileThread(val pis:PipedInputStream,val pos:PipedOutputStream):Thread(){

    override fun run() {
        while (true) {
            try {
                handle(pis)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    fun handle(stream: InputStream){
        val size = stream.read(HandleUtil.SINGLEBYTE)
        println("dispach,singleByte,size:" + size)
        val type = HandleUtil.SINGLEBYTE[0]
        when (type) {
            ProtocolField.fileGet -> {
                var fileSize = 0
                try {
                    fileSize = stream.read(HandleUtil.DOUBLEBYTE)
                    val dataSize = IntConvertUtils.getShortByByteArray(HandleUtil.DOUBLEBYTE)
                    if (dataSize <= 0) {
                        return
                    }
                    val data = HandleUtil.read(stream, dataSize)
                    //val file = HandleUtil.gson.fromJson(data, FileInfo::class.java)
                    //commandSwitch(command)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            ProtocolField.fileGetOK -> {

            }
        }

    }
}
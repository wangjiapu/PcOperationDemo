package thread

import Utils.HandleUtil
import Utils.IntConvertUtils
import beans.FileDescribe
import beans.FileInfo
import beans.PackByteArray

import beans.ProtocolField
import java.io.*
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque


object FileInputThread:Thread(){

    private var key:Short=0
    private val Perparedfiles=HashMap<Short,File>()

    private val messageQueue = LinkedBlockingDeque<PackByteArray>(1024)

    private val out = OutputThread


    fun addMessage(byteArray: PackByteArray) {
        messageQueue.putLast(byteArray)
    }


    override fun run() {


        try {
            while (!Thread.interrupted()){
                handle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun handle(){
        val pba=messageQueue.pop()
        val type = pba.flag
        when (type) {
            ProtocolField.fileGet -> {
                val fileDescibe=pba.body.toString()
                val fileInfos = HandleUtil.gson.fromJson(fileDescibe, Array<FileInfo>::class.java)
                val result=register2Map(fileInfos)

                if (result!=null){
                    val resultBytes=result.toByteArray(Charset.forName("UTF-8"))
                    val pack=PackByteArray(ProtocolField.fileSendOK,
                            IntConvertUtils.getShortByByteArray(resultBytes),resultBytes)
                    out.addMessage(pack)
                }
            }

            ProtocolField.fileGetOK->{
                sendFile()
            }
        }

    }


    /**
     * add file to Perparation Queue
     */
    private fun register2Map(fileInfos: Array<FileInfo>?):String? {
        if (fileInfos!=null){
            val map= HashMap<String,Short>()
            for (fileInfo in fileInfos) {
                val file = File(fileInfo.path)
                val describe = FileDescribe()
                describe.fileSize = file.length()
                val name = file.name.substring(0, file.name.lastIndexOf("."))
                val fileType = file.name.substring(file.name.lastIndexOf(".") + 1,
                        file.name.length)
                describe.fileName = name
                describe.fileType = fileType
                Perparedfiles.put(key,file)
                map.put("${name}.${fileType}",key++)
            }

            return HandleUtil.gson.toJson(map)
        }
        return null
    }


    private fun sendFile() {

        val threadPool = Executors.newFixedThreadPool(Perparedfiles.size)


        for (entry in Perparedfiles.entries) {
            threadPool.execute({
                kotlin.run({
                    var count: Int
                    val sb=IntConvertUtils.getShortBytes(entry.key)
                    val inputStream = FileInputStream(entry.value)
                    while (true) {
                        val bytes = ByteArray(4096)
                        bytes[0]=sb[0]
                        bytes[1]=sb[1]
                        count = inputStream.read(bytes,2,4096)
                        if (count == -1)
                            break
                        if (count == 4094) {
                            val pack = PackByteArray(ProtocolField.fileGetBody,
                                    IntConvertUtils.getShortByByteArray(bytes), bytes)
                            out.addMessage(pack)
                        } else {
                            val newBytes = ByteArray(count)
                            var i = 0
                            while (i < count) {
                                newBytes[i] = bytes[i]
                                i++
                            }
                            val pack = PackByteArray(ProtocolField.fileGetBody,
                                    IntConvertUtils.getShortByByteArray(newBytes), newBytes)
                            out.addMessage(pack)
                        }
                    }

                    val pack = PackByteArray(ProtocolField.fileGetEnd,
                            IntConvertUtils.getShortByByteArray(sb), sb)
                    out.addMessage(pack)
                })
            })
        }

    }
}
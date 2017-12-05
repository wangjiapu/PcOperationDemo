package thread

import Utils.HandleUtil
import Utils.IntConvertUtils
import beans.FileCommand
import beans.FileDescribe

import beans.ProtocolField
import newBeans.FileInfo
import pcMain.Parameter
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class FileInputThread(val pis:PipedInputStream, val pos:PipedOutputStream):Thread(){
    private var FILE_GET=false  //接受文件标志位

    private val mFileQueue: Queue<File> = LinkedList<File>()

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
                val filedescibe=read(stream)
                val list = ArrayList<FileDescribe>()
                val fileInfos = HandleUtil.gson.fromJson(filedescibe, Array<beans.FileInfo>::class.java)
                for (fileInfo in fileInfos) {
                    val file = File(fileInfo.getPath())
                    val describe = FileDescribe()
                    describe.fileSize = file.length()
                    val name = file.getName().substring(0, file.getName().lastIndexOf("."))
                    val type = file.getName().substring(file.getName().lastIndexOf(".") + 1,
                            file.getName().length)
                    describe.fileName = name
                    describe.fileType = type
                    list.add(describe)
                    mFileQueue.add(File(fileInfo.path))
                }
                pos.write( HandleUtil.splicing(ProtocolField.fileGetOK,HandleUtil.gson.toJson(list)))
                FILE_GET=true
            }

            ProtocolField.fileHead->{
                if (FILE_GET){
                   sendFile()
                }
            }

        }

    }


    private fun sendFile() {
        while (mFileQueue.isEmpty()) {
            var count = 0
            val inputStream = FileInputStream(mFileQueue.poll())
            while (true) {
                val bytes = ByteArray(1020)
                count = inputStream.read(bytes)
                if (count == -1)
                    break
                if (count == 1020){
                    pos.write(HandleUtil.splicing(ProtocolField.fileBody,bytes))
                    pos.flush()
                }
                else {
                    val newBytes = ByteArray(count)
                    var i = 0
                    i = 0
                    while (i < count) {
                        newBytes[i] = bytes[i]
                        i++
                    }
                    pos.write(HandleUtil.splicing(ProtocolField.fileBody,newBytes))
                    pos.flush()
                }
            }
            inputStream.close()
        }
    }

    private fun read(input:InputStream):String ? {
        //接收文件
        var fileSize = 0
        var result=""
        try {
            fileSize = input.read(HandleUtil.DOUBLEBYTE)
            val dataSize = IntConvertUtils.getShortByByteArray(HandleUtil.DOUBLEBYTE)
            if (dataSize <= 0) {
                return null
            }
            result = HandleUtil.read(input , dataSize)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

}
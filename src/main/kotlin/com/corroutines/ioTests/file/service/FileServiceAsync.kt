package com.corroutines.ioTests.file.service

import kotlinx.coroutines.*
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam

@Component
class FileServiceAsync(
        private val resourceLoader: ResourceLoader,
) {

    suspend fun processImages() =
            coroutineScope {
                val images = getFiles()
                val compressedImages =
                        withContext(Dispatchers.IO){
                            images.asSequence().map {
                                async {
                                    logThreadNameSuspended()
                                    compress(it, "/tmp/folderTeste/", 50)
                                }
                            }.toList()
                        }
                return@coroutineScope compressedImages.awaitAll()
            }


    private suspend fun getFiles(): List<Pair<String, File>> {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath:images/*.jpg").map { it.filename!! to it.file }
    }

    private suspend fun compress(image: Pair<String, File>, folderName: String, compressionQuality: Int): Pair<String, File> {
        val receivedImage = ImageIO.read(image.second)
        val compressedImageFile = File.createTempFile(folderName, image.first)
        val os = FileOutputStream(compressedImageFile)
        val fileExtension = getImageExtension(image.first)
        val writers = ImageIO.getImageWritersByFormatName(fileExtension)
        val writer = writers.next()
        val ios = ImageIO.createImageOutputStream(os)
        writer.output = ios
        val param = writer.defaultWriteParam
        param.compressionMode = ImageWriteParam.MODE_EXPLICIT
        param.compressionQuality = compressionQuality.toFloat() / 100
        writer.write(null, IIOImage(receivedImage, null, null), param)
        os.close()
        ios.close()
        writer.dispose()
        return image.first to compressedImageFile
    }

    private suspend fun getImageExtension(filename: String) = filename.replaceBeforeLast(".", "").replace(".", "")

    private suspend fun logThreadNameSuspended() = println("Current Thread: ${Thread.currentThread().name }")
}
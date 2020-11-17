package com.corroutines.ioTests.file.service

import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam

@Component
class FileService(
        private val resourceLoader: ResourceLoader,
) {

    val FILE_PATH = "classpath:images/*.jpg"

    suspend fun processImages() {
        val images = getFiles()
        val compressedImages = images.map { compress(it, "/tmp/folderTeste/", 50) }
        println(compressedImages)
    }

    private suspend fun getFiles(): List<Pair<String, File>> {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(FILE_PATH).map { it.filename!! to it.file }
    }

    suspend fun processOneImage() {
        val image = getOneFile()
        val compressedImage = compress(image, "/tmp/folderTeste/", 50)
    }


    private suspend fun getOneFile(): Pair<String, File> {
        val resource = resourceLoader.getResource(FILE_PATH)
        return resource.filename!! to resource.file
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
        return Pair(image.first, compressedImageFile)
    }

    private suspend fun getImageExtension(filename: String) = filename.replaceBeforeLast(".", "").replace(".", "")
}
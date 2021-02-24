package com.project.vllo.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.project.vllo.model.Item
import java.util.concurrent.TimeUnit

class GetFilesFromGallery {
    @SuppressLint("Recycle")
    fun listOfAll(context: Context): ArrayList<String> {
        val listOfAll = ArrayList<String>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        val cursor = context.contentResolver.query(uri, projection, null,null,null)
        val column_index_data = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while(cursor?.moveToNext()!!){
            val ablosutePathOfImage = cursor.getString(column_index_data!!)
            listOfAll.add(ablosutePathOfImage)
        }
        return listOfAll
    }

    fun listOfVideo(context: Context): MutableList<Item> {
        val itemList = mutableListOf<Item>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE
        )
        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES).toString()
        )
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
        val query = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val mime = cursor.getString(mimeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                itemList += Item(contentUri, name, duration, size, mime)
            }
        }
        return itemList
    }

    fun listOfPhoto(context: Context): MutableList<Item> {
        val itemList = mutableListOf<Item>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE
        )
        val selection =
                "${MediaStore.Images.Media.MIME_TYPE} =? OR " +
                "${MediaStore.Images.Media.MIME_TYPE} =? OR " +
                "${MediaStore.Images.Media.MIME_TYPE} =?"
        val selectionArgs = arrayOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("png"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)
                val mime = cursor.getString(mimeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                itemList += Item(contentUri, name, null, size, mime)
            }
        }

        return itemList
    }

    fun listOfGif(context: Context): MutableList<Item> {
        val itemList = mutableListOf<Item>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE
        )
        val selection = "${MediaStore.Images.Media.MIME_TYPE} =?"
        val selectionArgs = arrayOf(
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif")
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)
                val mime = cursor.getString(mimeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                itemList += Item(contentUri, name, null, size, mime)
            }
        }

        return itemList
    }
}
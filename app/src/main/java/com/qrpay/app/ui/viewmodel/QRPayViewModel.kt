package com.qrpay.app.ui.viewmodel

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qrpay.data.database.entity.User
import com.qrpay.data.model.QRCodeContent
import com.qrpay.data.repo.DataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import javax.inject.Inject

@HiltViewModel
class QRPayViewModel @Inject constructor(
    private val repo: DataRepo
) : ViewModel() {
    private var _isShowLoadingView = MutableStateFlow(false)
    val isShowLoadingView = _isShowLoadingView

    private var _isUpdatePhone = MutableStateFlow(false)
    val isUpdatePhone = _isUpdatePhone

    private var _isCreatedQRCode = MutableStateFlow(false)
    val isCreatedQRCode = _isCreatedQRCode

    private var _bitmapQRCode = MutableStateFlow<Bitmap?>(null)
    val bitmapQRCode = _bitmapQRCode

    private var _isNumberPhoneAvailable = MutableStateFlow(repo.getIsNumberPhoneAvailable())
    val isNumberPhoneAvailable = _isNumberPhoneAvailable

    private var _userApp = MutableStateFlow<User?>(null)
    val userApp = _userApp

    fun updateStatusCreatedQR(isStatus: Boolean) {
        _isCreatedQRCode.value = isStatus
    }

    fun showUpdatePhone(isStatus: Boolean) {
        _isUpdatePhone.value = isStatus
    }

    fun setBitmapQRCode(bitmap: Bitmap) {
        _bitmapQRCode.value = bitmap
    }

    fun createQRCode(qrCode: QRCodeContent) {
        _bitmapQRCode.value = repo.createQRCode(qrCode)
    }

    fun setIsNumberPhoneAvailable(isStatus: Boolean) {
        repo.setIsNumberPhoneAvailable(isStatus)
        _isNumberPhoneAvailable.value = isStatus
    }

    fun setUser(user: User) {
        viewModelScope.launch {
            repo.setUser(user)
            getUserInDB()
        }
    }

    fun getUserInDB() {
        if (_isNumberPhoneAvailable.value) {
            viewModelScope.launch {
                _userApp.value = repo.getUser()
            }
        }
    }

    fun updateUserInDB(user: User) {
        if (_isNumberPhoneAvailable.value) {
            viewModelScope.launch {
                repo.updateUser(user = user)
                _userApp.value = repo.getUser()
            }
        }
    }

    fun shareOtherApp(context: Context, bitmap: Bitmap) {
        _isShowLoadingView.value = true
        viewModelScope.launch {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                // Example: content://com.google.android.apps.photos.contentprovider/...
                putExtra(
                    Intent.EXTRA_STREAM,
                    convertBitmapToUri(context = context, bitmap = bitmap)
                )
                type = "image/jpeg"
            }
            delay(1000)
            withContext(Dispatchers.Main) {
                context.startActivity(Intent.createChooser(shareIntent, null))
                _isShowLoadingView.value = false
            }
        }
    }

    private fun convertBitmapToUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, null, null)
        return Uri.parse(path.toString())
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveInStorage(bitmap: Bitmap, context: Context) {
        _isShowLoadingView.value = true
        viewModelScope.launch {
            val resolver: ContentResolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis())
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/QRPAYByLongHRK"
            )

            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            writeBitmapToUri(context, bitmap, imageUri)
            delay(1000)
            withContext(Dispatchers.Main) {
                _isShowLoadingView.value = false
                Toast.makeText(context, "Save photo completed", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Throws(FileNotFoundException::class)
fun writeBitmapToUri(context: Context, bitmap: Bitmap, outputUri: Uri?): Uri? {
    var outputStream: OutputStream? = null
    try {
        outputStream = context.contentResolver.openOutputStream(outputUri!!)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    } finally {
        closeSafe(outputStream)
    }
    return outputUri
}

private fun closeSafe(closeable: Closeable?) {
    try {
        closeable?.close()
    } catch (ignored: IOException) {
        Log.e("TAG_ERROR", ignored.toString())
    }
}
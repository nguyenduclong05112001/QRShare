package com.qrpay.data.repo

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.qrpay.data.consts.AppConst.IS_USER_ENTER_NUMBER_PHONE
import com.qrpay.data.database.AppDatabase
import com.qrpay.data.database.entity.User
import com.qrpay.data.model.QRCodeContent
import com.qrpay.data.preference.AppSharedPreference
import javax.inject.Inject

class DataRepo @Inject constructor(
    private val refs: AppSharedPreference,
    private val db: AppDatabase
) {
    fun createQRCode(qrCode: QRCodeContent): Bitmap?{
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(
                "2|99|${qrCode.numberPhone}|0|0|0|0|${qrCode.amount}|${qrCode.description}",
                BarcodeFormat.QR_CODE,
                200,
                200
            )
            val barcodeEncoder = BarcodeEncoder()
            return barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return null
    }

    fun getIsNumberPhoneAvailable() =
        refs.getBolPreference(IS_USER_ENTER_NUMBER_PHONE)

    fun setIsNumberPhoneAvailable(isStatus: Boolean) {
        refs.setBolPreference(IS_USER_ENTER_NUMBER_PHONE, isStatus)
    }

    suspend fun setUser(user: User) {
        db.getUserDb().insertUser(user)
    }

    suspend fun updateUser(user: User) {
        db.getUserDb().updateUser(user)
    }

    suspend fun getUser() = db.getUserDb().getUser()
}
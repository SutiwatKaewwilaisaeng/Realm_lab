package com.example.likelab.realm

import android.graphics.Bitmap
import android.os.Parcel
import androidx.core.app.Person
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class TableUserProfile(
    @PrimaryKey
    var autoId:Int? =0,
    var date: String? = "",
    var time: String? = "",
    var image: String? = "",
    @Required
    var name: String? = ""
) : RealmObject()
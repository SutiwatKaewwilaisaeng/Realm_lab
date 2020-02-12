package com.example.likelab.realm

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class TableUser(
) : RealmObject() {
    @PrimaryKey
    @Required
    var userName: String? = ""
    var passWord: String? = ""
    var confirmPassword: String? = ""
    @Required
    var status: Boolean? = false
}
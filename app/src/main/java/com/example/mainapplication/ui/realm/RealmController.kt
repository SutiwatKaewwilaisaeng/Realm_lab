package com.example.mainapplication.ui.realm

import android.content.Context
import android.content.Intent
import android.util.Log.e
import android.widget.Toast
import com.example.likelab.realm.TableUser
import com.example.likelab.realm.TableUserProfile
import com.example.mainapplication.ui.PathUtil
import io.realm.Realm

class RealmController {

    fun login(username: String, password: String): TableUser? {
        val realm = Realm.getDefaultInstance()
        return try {
            val userInfo = realm.where(TableUser::class.java)
                .equalTo("userName", username)
                .equalTo("passWord", password)
                .findFirst()
            if (userInfo != null) {
                realm.copyFromRealm(userInfo)
            } else {
                null
            }
        } catch (ex: Exception) {
            ex.printStackTrace()

            null
        } finally {
            realm?.close()
        }
    }

    fun isHasLogin(): TableUser? {
        val realm = Realm.getDefaultInstance()
        return try {
            val userInfo = realm.where(TableUser::class.java)
                .equalTo("status", true)
                .findFirst()

            if (userInfo != null) {
                realm.copyFromRealm(userInfo)
            } else {
                null
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        } finally {
            realm?.close()
        }
    }

    fun updateStatusLogin(tableUser: TableUser, statusLogin: Boolean): Boolean {
        val realm = Realm.getDefaultInstance()
        return try {
            if (!realm.isInTransaction) {
                realm.beginTransaction()
            }

            tableUser.status = statusLogin
            realm.insertOrUpdate(tableUser)
            realm.commitTransaction()
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        } finally {
            realm?.close()
        }
    }

    fun updateStatusEdit(
        tableUserProfile: TableUserProfile,
        pathImage: String,
        name: String,
        time: String,
        date: String,
        autoId: Int
    ): TableUserProfile? {
        val realm = Realm.getDefaultInstance()
        return try {
            if (!realm.isInTransaction) {
                realm.beginTransaction()
            }
            tableUserProfile.autoId = autoId
            tableUserProfile.name = name
            tableUserProfile.date = date
            tableUserProfile.time = time
            tableUserProfile.image = pathImage


            e("updateStatusEdit", " autoID :${autoId}name:${name}\n date:${date}\n time:${time}\n image:${pathImage}")
            realm.insertOrUpdate(tableUserProfile)
            realm.commitTransaction()

            return tableUserProfile
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        } finally {
            realm.close()
        }
    }


}
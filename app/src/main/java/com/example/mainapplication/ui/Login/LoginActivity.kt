package com.example.likelab.Login

import android.os.Bundle
import android.util.Log.e
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.likelab.RegisterBottomSheetDialog
import com.example.likelab.realm.TableUser
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_login.*
import android.content.Intent
import android.widget.Toast
import com.example.likelab.MainActivity
import com.example.likelab.R
import com.example.mainapplication.ui.realm.RealmController
import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    var registerBottomSheet = RegisterBottomSheetDialog()
    var realm: Realm? = null
    var tableUser = TableUser()
    var realmLogin:TableUser? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Realm.init(this)
//        val config = RealmConfiguration.Builder()
//            .deleteRealmIfMigrationNeeded()
//            .build()
//        Realm.setDefaultConfiguration(config)
        textViewRegister.setOnClickListener {
            showBottomSheet()
        }
        buttonLogin.setOnClickListener {
            val username = editTextUserNameLoginPage.text.toString()
            val password = editTextPasswordLoginPage.text.toString()
            when {
                !checkStringIsNotEmpty(username) -> {
                    editTextUserNameLoginPage.error = "Please enter username"
                    editTextUserNameLoginPage.requestFocus()
                }
                !checkStringIsNotEmpty(password)-> {
                    editTextPasswordLoginPage.error = "Please enter password"
                    editTextPasswordLoginPage.requestFocus()
                }
                else -> {
                    val userInfo = RealmController().login(username, password)
                    if (userInfo != null) {
                        RealmController().updateStatusLogin(userInfo, true)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("username", userInfo.userName)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Username or Password invalid", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        val userInfoHasLogin = RealmController().isHasLogin()
        if (userInfoHasLogin != null && userInfoHasLogin.status == true) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", userInfoHasLogin.userName)
            startActivity(intent)
            finish()
        }
//        initView()

    }

    private fun initView() {
        textViewRegister.setOnClickListener {
            showBottomSheet()
        }

        buttonLogin.setOnClickListener{
            try {
                Realm.init(this)
                realm = Realm.getDefaultInstance()
                if (!realm!!.isInTransaction){
                    realm?.beginTransaction()
                }
                realmLogin = realm!!.where(TableUser::class.java).equalTo("userName",editTextPasswordLoginPage.text.toString()).equalTo("passWord",editTextPasswordLogin.text.toString()).findFirst()
                var response = ""
                var userName = editTextUserNameLoginPage.text.toString()
                var passWord = editTextPasswordLoginPage.text.toString()

                e("realmregis", response)
                e("realmregis", userName)
                e("realmregis", passWord)

                when {
                    !checkStringIsNotEmpty(userName) -> {
                        editTextUserNameLoginPage.error = "Please enter username"
                        editTextUserNameLoginPage.requestFocus()
                    }
                    !checkStringIsNotEmpty(passWord)-> {
                        editTextPasswordLoginPage.error = "Please enter password"
                        editTextPasswordLoginPage.requestFocus()
                    }
                    else -> {
                        val userInfo = RealmController().login(userName,passWord)
            //  if (realmLogin!!.userName == editTextUserNameLogin.text.toString() && realmLogin!!.passWord == editTextPasswordLogin.text.toString()) {
                        if (userInfo != null){
                            RealmController().updateStatusLogin(userInfo, true)
                            val intent = Intent(this, MainActivity::class.java)
                                .putExtra("nameLogin", userInfo.userName)
                            startActivity(intent)
                        } else {
                            editTextUserNameLoginPage.setError("กรุณากรอก Username ที่ถูกต้อง")
                            editTextPasswordLoginPage.setError("กรุณากรอก Password ที่ถูกต้อง")
                        }
                    }
                }

            }catch (ex:Exception){
                ex.printStackTrace()
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()

            }finally {
                realm?.close()
            }
        }
        var checkLogin = RealmController().isHasLogin()
        if (checkLogin != null){
            RealmController().updateStatusLogin(checkLogin, false)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", checkLogin.userName)
            startActivity(intent)
            finish()
        }
    }

    private fun showBottomSheet() {
        registerBottomSheet = RegisterBottomSheetDialog().newInstance()
        registerBottomSheet.show(supportFragmentManager, registerBottomSheet.TAG)
    }
    private fun checkStringIsNotEmpty(str: String): Boolean {
        return str != null && str.isNotEmpty() && str.isNotBlank()
    }

}

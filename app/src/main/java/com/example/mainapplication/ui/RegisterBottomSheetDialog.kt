package com.example.likelab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.likelab.realm.TableUser
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception


class RegisterBottomSheetDialog : BottomSheetDialogFragment() {

    val TAG = "RegisterBottomSheetDialog"
    var realm: Realm? = null
    var dataRegister = TableUser()

    fun newInstance(): RegisterBottomSheetDialog {
        val regisActivity = RegisterBottomSheetDialog()
        return regisActivity
    }

//    fun getDataForLogin() {
//        try {
//            Realm.init(context)
//            val realmConfiguration = RealmConfiguration.Builder().build()
//            Realm.setDefaultConfiguration(realmConfiguration)
//            realm = Realm.getDefaultInstance()
//            val realmUser = realm!!.where<TableUser>().findAllAsync()
//            var response = ""
//            realmUser.forEach {
//                response = response + "Name: ${it.userName}, pass: ${it.passWord}" + "\n"
//            }
//            realmUser.load()
//            realm!!.close()
//            e("realmregis", response)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            Realm.init(context)

            buttonRegister.setOnClickListener {
                realm = Realm.getDefaultInstance()

                var textNameRegister = editTextNameRegisterPage.text.toString()
                var textPasswordRegister = editTextPasswordRegisterPage.text.toString()
                var textConfirmPasswordRegister = editTextConfirmPasswordRegisterPage.text.toString()
                dataRegister.userName = textNameRegister
                dataRegister.passWord = textPasswordRegister
                dataRegister.confirmPassword = textConfirmPasswordRegister
                dataRegister.status = false

                if (dataRegister.passWord == dataRegister.confirmPassword) {
                    Toast.makeText(context, "พาสเวริด์ถูกต้อง", Toast.LENGTH_SHORT).show()
//                realm!!.insert(dataRegister)
                    realm!!.beginTransaction()
                    realm!!.insert(dataRegister)
                    realm!!.commitTransaction()

                    dialog!!.dismiss()
                    realm!!.close()
                } else {
                    editTextPasswordRegister.setError("พาสเวริด์กับคอนเฟริม์พาสเวริด์ไม่ตรงกัน")
                    editTextPasswordRegister.setText("")
                    editTextConfirmPasswordRegister.setText("")
                    editTextConfirmPasswordRegister.setError("พาสเวริด์กับคอนเฟริม์พาสเวริด์ไม่ตรงกัน")
                }
            }

        }catch (ex:Exception){
            ex.printStackTrace()
            Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_register, container, false)

        return view
    }

}

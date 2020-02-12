package com.example.likelab.Edit

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import kotlinx.android.synthetic.main.activity_edit.*


import android.graphics.Bitmap

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.likelab.realm.TableUserProfile
import com.example.mainapplication.ui.PathUtil
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import io.realm.RealmResults
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R
import androidx.appcompat.app.ActionBar
import com.example.likelab.MainActivity
import com.example.mainapplication.EditProfile.EditProfileFragment
import com.example.mainapplication.ui.realm.RealmController
import kotlin.Exception


class EditActivity : AppCompatActivity() {
    private var realm: Realm? = null
    var preferences: SharedPreferences? = null
    private var MY_PREFS = "my_prefs"
    lateinit var toolbar: ActionBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.likelab.R.layout.activity_edit)
        toolbar = supportActionBar!!
        var name = intent.getStringExtra("username")

        Realm.init(this)
        editTextChange()
    }

    companion object {
        fun newInstance(): EditActivity {
            val editActivity = EditActivity()

            return editActivity
        }
    }


    fun editTextChange() {

        val valuename = intent.getStringExtra("name")
        val valuenameTitle = intent.getStringExtra("username")
        val valuedate = intent.getStringExtra("date")
        val valueimage = intent.getStringExtra("image")
        var autoId: Int

        autoId = try {
            realm?.where(TableUserProfile::class.java)?.findAll()?.sort("autoId", Sort.DESCENDING)
                .toString().length

        } catch (ex: Exception) {
            ex.printStackTrace()
            0
        }
        e("value", valuename)

        editTextNameEditItem.setText("$valuename")
        textViewDateTimeTitle.setText("${getString(com.example.likelab.R.string.date)}${valuedate}")
        textViewTimeEditTitle.setText("${getString(com.example.likelab.R.string.time)} ${timeFormater()}")
        Glide.with(this).load(valueimage).into(imageViewProfileEditItem)
        imageViewProfileEditItem.setOnClickListener {
            addimagecontact()
        }

        e("name time date", "${"Name$valuename"}")
        buttonConfirmEditReal.setOnClickListener  {
            try{

                val tableUpdate = TableUserProfile()
                var pathImageUpdate = preferences?.getString("ImageUriEdit", "")
                if (pathImageUpdate.isNullOrEmpty()){
                    pathImageUpdate = "${valueimage}"
                }
                tableUpdate.autoId = autoId.minus(1)
                RealmController().updateStatusEdit(
                    tableUpdate,
                    pathImageUpdate,
                    editTextNameEditItem.text.toString(),
                    timeFormater(),
                    EditProfileFragment.newInstance().dateFormater(),
                    autoId.minus(1)
                )
                Toast.makeText(this,"Update Complete",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                    .putExtra("username",valuenameTitle)
                startActivity(intent)

            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }

    private fun addimagecontact() {
        val myIntent = Intent(Intent.ACTION_GET_CONTENT, null)
        myIntent.type = "image/*"
        startActivityForResult(myIntent, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> addimagecontact()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
            try {
                // We need to recyle unused bitmaps
                var stream: InputStream? = contentResolver.openInputStream(
                    data?.getData()!!
                )
                PathUtil.getRealPathFromURI_API19(this, data!!.data)
                e(
                    "pathEdit", PathUtil.getRealPathFromURI_API19(this, data!!.data)
                )
                val bitmap = BitmapFactory.decodeStream(stream)
                stream?.close()
                var resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
                imageViewProfileEditItem.setImageBitmap(resizedBitmap)
                preferences = getSharedPreferences(
                    MY_PREFS,
                    Context.MODE_PRIVATE
                )
                var editor = preferences!!.edit()
                editor.putString(
                    "ImageUriEdit", PathUtil.getRealPathFromURI_API19(this, data.data)
                )
                editor.commit()
//                e("Bitmap", resizedBitmap.toString())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun timeFormater(): String {
        try {
            val stringTime = SimpleDateFormat("h:mm:ss a").format(Date())
            return "${stringTime}"
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message ?: "Something wrong"
        }
    }
}

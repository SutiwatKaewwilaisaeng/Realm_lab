package com.example.mainapplication.EditProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.likelab.List.ListAdapter
import com.example.likelab.R

import com.example.likelab.realm.TableUserProfile
import com.example.mainapplication.ui.PathUtil

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Arrays.sort
import kotlin.collections.ArrayList

class EditProfileFragment : Fragment(), ListAdapter.Listener {


    var realm: Realm? = null
    var imageView: ImageView? = null
    var tableUserProfile = TableUserProfile()
    var byteArray: ByteArray? = null
    var myIntent: Intent? = null
    var uri: String? = null
    var preferences: SharedPreferences? = null
    private var search: RealmResults<TableUserProfile>? = null
    private var MY_PREFS = "my_prefs"
    var photoFile: File? = null
    var imageFile: File? = null
    var mCurrentPhotoPath: String? = null
    val REQUEST_PERMISSION = 401
    val list = ArrayList<TableUserProfile>()
    private var adapter: ListAdapter? = null
    var nextId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        imageView = view.circleImage
        return view
    }

    companion object {
        fun newInstance(): EditProfileFragment {
            val fragment = EditProfileFragment()
            var bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = this.arguments
        val name = bundle?.getString("username")

        circleImage.setOnClickListener {
            onItemClicked()
        }

        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        realm = Realm.getDefaultInstance()

        textViewDate.text = "${dateFormater()}"
        textViewTime.text = "${timeFormater()}"

        circleImage.setImageResource(R.drawable.avatar)


//        val path: String = context!!.filesDir.absolutePath
//        val file = File(path)
//        Picasso.with(context)
//            .load(file)
//            .resize(100,100)
//            .into(circleImage)
//            circleImage.setImageBitmap(R.mipmap.ic_launcher)
//        Picasso.with(context)
//            .load(R.drawable.avatar)
//            .resize(100, 100)
//            .into(circleImage)

        buttonAdd.setOnClickListener {
            try {
                val stringValue = preferences?.getString("ImageUri", "")
                tableUserProfile = TableUserProfile()
                realm = Realm.getDefaultInstance()
                if (realm?.isInTransaction == false) {
                    realm?.beginTransaction()
                }
//                realm!!.executeTransaction(object : Realm.Transaction {
//                    override fun execute(realm: Realm?) {
//                        var currentIdNum = realm?.where(TableUserProfile::class.java)?.max("autoId")
//                        if (currentIdNum == null){
//                            nextId = 1
//
//                        }else{
//                            nextId = currentIdNum.toInt()+1
//                        }
//                    }
//
//                })
                var autoId: Int

                autoId = try {
                    realm?.where(TableUserProfile::class.java)?.findAll()?.sort("autoId", Sort.DESCENDING).toString().length

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    0
                }
                tableUserProfile.autoId = autoId++
                tableUserProfile.name = editTextName.text.toString()
                tableUserProfile.date = dateFormater()
                tableUserProfile.time = timeFormater()
                tableUserProfile.image = stringValue

                preferences = activity?.getSharedPreferences(
                    MY_PREFS,
                    Context.MODE_PRIVATE
                )
                var editor = preferences!!.edit()
                editor.putInt(
                    "autoId", autoId)

                editor.commit()


                realm?.insert(tableUserProfile)
                realm?.commitTransaction()

//                e("autoId",tableUserProfile.autoId.toString())
                Toast.makeText(requireContext(), "ADD Complete", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
            } finally {
                realm?.close()
            }

//            try {
//                var stringValue = preferences?.getString("ImageUri", "")
//                e("WTFFFF", stringValue)
//                var textUserName = editTextName.text.toString()
//                realm = Realm.getDefaultInstance()
//                realm!!.beginTransaction()
//                tableUserProfile.name = "${textUserName}"
//                tableUserProfile.date = "${dateFormater()}"
//                tableUserProfile.time = "${timeFormater()}"
//                e("newByteee", stringValue.toString())
//                tableUserProfile.image = stringValue
//                if (tableUserProfile.name.isNullOrEmpty()) {
//                    realm!!.beginTransaction()
//                    realm!!.insert(tableUserProfile)
//                    realm!!.copyToRealmOrUpdate(tableUserProfile)
//                    realm!!.commitTransaction()
////                    realm!!.close()
//                    val size = realm!!.where<TableUserProfile>().count()
//                    search =
//                        realm!!.where<TableUserProfile>().equalTo("name", tableUserProfile.name)
//                            .findAll()
//                    e("register", size.toString())
//                    e("search", search.toString())
//                    Toast.makeText(context, "Change Complete", Toast.LENGTH_SHORT).show()
//                    editTextName.visibility = View.GONE
//                    textViewName.setText("Name : ${search!![0]!!.name}")
//
//                } else if (textUserName == realm!!.where<TableUserProfile>().equalTo(
//                        "name",
//                        tableUserProfile.name
//                    ).findAll().toString()
//                ) {
//                    editTextName.setError("ชื่อที่เเก้ไขซ้ำกัน")
//                } else {
//                    realm = Realm.getDefaultInstance()
////                    realm!!.close()
//                    val size = realm!!.where<TableUserProfile>().count()
//
//                    e("size", size.toString())
//                    e("search", search.toString())
//
//                    editTextName.visibility = View.GONE
//                    textViewName.setText("Name : ${textUserName}")
//                    realm!!.insert(tableUserProfile)
//                    realm!!.copyToRealmOrUpdate(tableUserProfile)
//                    realm!!.commitTransaction()
//                    Toast.makeText(context, "Change Complete", Toast.LENGTH_SHORT).show()
//
//                }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            } finally {
//                getModelList()
////
//                realm!!.close()
//            }
        }
    }

    private fun addimagecontact() {
        myIntent = Intent(Intent.ACTION_GET_CONTENT, null)
        e("Uri", uri.toString())
        photoFile = createImageFile()
        mCurrentPhotoPath = photoFile!!.absolutePath
        if (photoFile != null) {
            val photoUri = FileProvider.getUriForFile(
                context!!,
                "kerry.express.th.mobile.camera_test.fileprovider",
                photoFile!!
            )
            e("Photo", photoUri.path)
            myIntent!!.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//            myIntent!!.type = "image/*"
            startActivityForResult(myIntent, 1)
        }
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

    private fun createImageFile(): File {
        val timestamp = System.currentTimeMillis() / 1000
        val storageDir = this.activity?.getExternalFilesDir("/source")
        return createTempFile(
            "cam-alive-$timestamp",
            ".jpg",
            storageDir
        )
    }

    //     fun  getPath(context:Context,uri:Uri ) {
//    var result = null
//    var proj = { MediaStore.Images.Media.DATA }
//    var cursor = context.getContentResolver( ).query( uri, null, null, null, null );
//    if(cursor != null){
//        if ( cursor.moveToFirst( ) ) {
//            var column_index = cursor.getColumnIndexOrThrow( null )
//            result = cursor.getString( column_index )
//        }
//        cursor.close();
//    }
//    if(result == null) {
//        result = "Not found"
//    }
//    return result
//}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
            try {
                realm = Realm.getDefaultInstance()

                PathUtil.getRealPathFromURI_API19(context, data!!.data)
                e(
                    "path", PathUtil.getRealPathFromURI_API19(context, data.data)
                )
                var stream: InputStream? = requireActivity().contentResolver.openInputStream(
                    data.data!!
                )
                val bitmap = BitmapFactory.decodeStream(stream)
                stream?.close()
                var resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
                circleImage.setImageBitmap(resizedBitmap)

                val streamout = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, streamout)
                byteArray = streamout.toByteArray()
                bitmap.recycle()


                preferences = activity?.getSharedPreferences(
                    MY_PREFS,
                    Context.MODE_PRIVATE
                )
                var editor = preferences!!.edit()
                editor.putString(
                    "ImageUri", PathUtil.getRealPathFromURI_API19(context, data.data)
                )
                editor.commit()

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getModelList(): List<TableUserProfile> {
        try {
            realm = Realm.getDefaultInstance()
//            realm?.beginTransaction()
            val results = realm!!
                .where(TableUserProfile::class.java)
                .findAll()
//            e("GGWP", Gson().toJson(realm!!.copyFromRealm(results)))
            list.addAll(realm!!.copyFromRealm(results))


            adapter?.updateView(list)

            adapter?.notifyDataSetChanged()

        } finally {
            realm!!.close()
        }
        return list
    }

    private fun onItemClicked() {
        val myIntent = Intent(Intent.ACTION_GET_CONTENT, null)
        myIntent.type = "image/*"
        startActivityForResult(myIntent, 1)
    }


    fun dateFormater(): String {
        try {
            val stringYear = SimpleDateFormat("yyyy").format(Date())
            val stringDayMonth = SimpleDateFormat("d MMMM").format(Date())
            return "$stringDayMonth ${stringYear.toInt() + 543}"
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message ?: "Something wrong"
        }
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

    override fun onItemClickDelete(tableUserProfile: TableUserProfile, position: Int) {

    }

    override fun onItemClickEdit(tableUserProfile: TableUserProfile, byteArray: String) {

    }

}



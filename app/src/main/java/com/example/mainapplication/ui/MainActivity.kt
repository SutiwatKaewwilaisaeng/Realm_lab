package com.example.likelab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.likelab.Edit.EditActivity
import com.example.likelab.List.ListAdapter
import com.example.likelab.pagerAdapter.pagerAdapter
import com.example.likelab.realm.TableUserProfile
import com.example.mainapplication.EditProfile.EditProfileFragment
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : LocalizationActivity(), ListAdapter.Listener {
    override fun onItemClickDelete(tableUserProfile: TableUserProfile, position: Int) {
    }

    override fun onItemClickEdit(tableUserProfile: TableUserProfile, byteArray: String) {
    }

    private val KEY_CURRENT_PAGE = "current_page"
    lateinit var toolbar: ActionBar
    private var isClickBottomNavigation = false
    private var intentLogin: String? = null
    private var image: ByteArray? = null
    private var name: String? = null
    private var date: String? = null
    private var time: String? = null
    private var MY_PREFS: String? = "MY PREF"
    private var islocalLanguage: Boolean = false

    private var typeSwitchLanguage: String? = null
    var preferences: SharedPreferences? = null
    val REQUEST_PERMISSION = 401
    private val localizationDelegate: LocalizationActivityDelegate by lazy {
        LocalizationActivityDelegate(
            this
        )
    }
    private var realm: Realm? = null
    private var listAdapter = ArrayList<TableUserProfile>()
    val list = ArrayList<TableUserProfile>()
    private var adapter: ListAdapter? = null


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_add -> {
                    name = intent.getStringExtra("username")
                    toolbar.title = "${getString(R.string.username)}  ${name}"

//                    EditActivity.newInstance().editTextChange(name!!)
//
                    view_pager.setCurrentItem(0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_list -> {
                    name = intent.getStringExtra("username")
                    toolbar.title = "${getString(R.string.username)}   ${name}"
//                    EditActivity.newInstance().editTextChange(name!!)
                    view_pager.setCurrentItem(1)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_setting -> {
                    name = intent.getStringExtra("username")
                    toolbar.title = "${getString(R.string.username)}   ${name}"
//                    EditActivity.newInstance().editTextChange(name!!)
                    view_pager.setCurrentItem(2)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionRequest()
        toolbar = supportActionBar!!
        name = intent.getStringExtra("username")

        if (savedInstanceState == null) {
            intentLogin = intent.getStringExtra("username")
            toolbar.title = "${getString(R.string.username)}   ${name}"
//             linkPicture = "R.drawable.avatar"


            val editProfileFragment = EditProfileFragment.newInstance()
            openFragment(editProfileFragment)
        }
        preferences = getSharedPreferences(
            MY_PREFS,
            Context.MODE_PRIVATE
        )
        typeSwitchLanguage = preferences?.getString("typeLanguage", "")

        view_pager.adapter =
            pagerAdapter(supportFragmentManager)

        view_pager.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }

        })
        view_pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        nav_view.menu.findItem(R.id.navigation_add).setChecked(true)

                    }
                    1 -> {
                        nav_view.menu.findItem(R.id.navigation_list).setChecked(true)
                    }
                    2 -> {
                        nav_view.menu.findItem(R.id.navigation_setting).setChecked(true)
                    }
                }
            }

        })
        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        e("tag", "wwwwwwwwwwwww")

        when (resultCode) {
            Constants.DIALOG_REQUEST_PROFILE -> {
                var valueName = data!!.getStringExtra("name")
                var valueDate = data!!.getStringExtra("date")
                var valueTime = data!!.getStringExtra("time")
                e("value", valueName + valueDate + valueTime)
                if (requestCode == 0) {
                    if (resultCode == Activity.RESULT_OK) {
                        var contents = data.getStringExtra("SCAN_RESULT")
                        e("tag", "contents: " + contents)
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        e("tag", "RESULT_CANCELED")
                    }
                }
            }
        }
    }

    private fun initView() {
        view_pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        nav_view.menu.findItem(0).setChecked(true)
                    }
                    1 -> {
                        nav_view.menu.findItem(1).setChecked(true)
                    }
                    2 -> {
                        nav_view.menu.findItem(2).setChecked(true)
                    }

                }
            }

        })
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        // Save current item of view pager.
        intentLogin = intent.getStringExtra("username")
        outState.putInt(KEY_CURRENT_PAGE, view_pager.getCurrentItem())

        toolbar.title = "${getString(R.string.username)}   ${name}"
        super.onSaveInstanceState(outState)

    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore current item of view pager.
        intentLogin = intent.getStringExtra("username")
        view_pager.setCurrentItem(savedInstanceState.getInt(KEY_CURRENT_PAGE))

        toolbar.title = "${getString(R.string.username)}   ${name}"

    }


    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.view_pager, fragment)
//        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun permissionRequest(): Boolean {
        val cameraPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val writeExtenal = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readExtenal = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val listPermissionNeeded = ArrayList<String>()

        if (cameraPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(android.Manifest.permission.CAMERA)
        if (writeExtenal != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (readExtenal != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionNeeded.toTypedArray(),
                REQUEST_PERMISSION
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                val perms = HashMap<String, Int>()

                perms[android.Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[android.Manifest.permission.READ_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    if (perms[android.Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED &&
                        perms[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED &&
                        perms[android.Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    ) {
//                        dispatchTakePictureIntent()
                        Log.e("PERMISSION", "GRANTED")
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                android.Manifest.permission.CAMERA
                            ) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            Log.e("PERMISSION", "REQUIRE")
                        }
                    }
                }
            }
        }
    }

}

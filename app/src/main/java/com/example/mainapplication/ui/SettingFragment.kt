package com.example.likelab

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log.e
import android.util.Log.i
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.localizationactivity.core.LanguageSetting.setDefaultLanguage
import com.akexorcist.localizationactivity.core.LanguageSetting.setLanguage
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.likelab.List.ListAdapter
import com.example.likelab.List.ListFragment
import com.example.likelab.Login.LoginActivity
import com.example.likelab.realm.TableUser
import com.example.likelab.realm.TableUserProfile
import com.example.mainapplication.ui.realm.RealmController
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_setting.*
import java.lang.Exception
import java.util.*

class SettingFragment : Fragment() {
    private var islocalLanguage: Boolean = false
    var realm: Realm? = null
    var typeLanguage = "eng"

    var MY_PREFS = "MyPREF"
    var preferences: SharedPreferences? = null
    val list = ArrayList<TableUserProfile>()
    private var adapter: ListAdapter? = null
    var intent: Intent? = null
    private var language = "EN"
    private val localizationDelegate: LocalizationActivityDelegate by lazy {
        LocalizationActivityDelegate(
            requireActivity()
        )
    }
    private var name: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    companion object {
        fun newInstance(): SettingFragment {
            val fragment = SettingFragment()
            var bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        e("typeSwitchLanguage",switchLanguage)
        textViewLanguage.setOnClickListener {
            if (!islocalLanguage) {
                islocalLanguage = true
                name = activity?.intent?.getStringExtra("username")
                e("language", "en$islocalLanguage")
                LocalizationActivityDelegate(activity!!).setLanguage(context!!, "en")
            } else {
                islocalLanguage = false
//                e("language", "th$islocalLanguage")
//                textViewLanguage.text = getString(R.string.language)
                LocalizationActivityDelegate(activity!!).setLanguage(context!!, "th")
            }
            preferences = activity?.getSharedPreferences(
                MY_PREFS,
                Context.MODE_PRIVATE
            )
            var editor = preferences!!.edit()
            editor.putString(
                "typeLanguage", typeLanguage
            )

            editor.commit()
        }
        textViewDelete.setOnClickListener {
            try {
                val realmConfiguration = RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build()
                Realm.setDefaultConfiguration(realmConfiguration)

                realm = Realm.getDefaultInstance()
                val results = realm!!
                    .where(TableUserProfile::class.java)
                    .findAll()
                realm?.executeTransaction { realm ->
                    results.deleteAllFromRealm()
                    Toast.makeText(context, "Delete Complete", Toast.LENGTH_SHORT).show()
                    e("delete", results.toString())
                }
                realm?.commitTransaction()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                realm?.close()
            }

        }
        textViewLogout.setOnClickListener {
           val userInfo = RealmController().isHasLogin()
            val username = intent?.getStringExtra("nameLogin")?:"no nameLogin"
            if (userInfo != null) {
                RealmController().updateStatusLogin(userInfo, false)
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }


}

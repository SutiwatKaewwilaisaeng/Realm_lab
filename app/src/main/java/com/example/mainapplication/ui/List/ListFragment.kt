package com.example.likelab.List

import android.content.Intent
import android.os.Bundle
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.likelab.Edit.EditActivity
import com.example.likelab.R

import com.example.likelab.realm.TableUserProfile
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_list.*
import java.lang.Exception

class ListFragment : Fragment(), ListAdapter.Listener {

    var list = ArrayList<TableUserProfile>()
    private var adapter: ListAdapter? = null
    var realm: Realm? = null
    var nametitle: String? = ""
//    var tableUserProfile :TableUserProfile? = null
//    var saveName:String?=""
//    var saveDate:String?=""
//    var saveTime:String?=""
//    var saveImage:String?=""

    companion object {
        fun newInstance(): ListFragment {
            val fragment = ListFragment()

            return fragment
        }
    }

    override fun onItemClickDelete(tableUserProfile: TableUserProfile, position: Int) {
        try {
            realm = Realm.getDefaultInstance()
            val resultUserProfile = realm!!
                .where(TableUserProfile::class.java).equalTo("name", tableUserProfile.name)
                .findAll()
            e("tableUserProfile", tableUserProfile.toString())
            e("position", position.toString())
            if (resultUserProfile != null) {
                if (!realm!!.isInTransaction) {
                    realm!!.beginTransaction()
                }
                list.remove(tableUserProfile)
                adapter!!.notifyItemRemoved(position)
                resultUserProfile.deleteAllFromRealm()
                realm!!.commitTransaction()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            realm!!.close()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getModelList()
        }
    }

    override fun onItemClickEdit(tableUserProfile: TableUserProfile, pathImage: String) {
        e(
            "clickedit", tableUserProfile.name + tableUserProfile.time!! +
                    tableUserProfile.date!! + pathImage
        )
        nametitle = activity?.intent?.getStringExtra("username")

        val name = tableUserProfile.name
        val autoid = tableUserProfile.autoId
        val date = tableUserProfile.date
        val time = tableUserProfile.time
        val image = pathImage
        val intent = Intent(context, EditActivity::class.java)
            .putExtra(
                "name",
                name
            ).putExtra("time", time).putExtra("date",date)
            .putExtra("image", image).putExtra("autoId",autoid).putExtra("username",nametitle)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getModelList()
    }


    fun getModelList(): List<TableUserProfile> {
        try {
            realm = Realm.getDefaultInstance()
//            realm?.beginTransaction()
            val results = realm!!
                .where(TableUserProfile::class.java)
                .findAll()
//            e("GGWP", Gson().toJson(realm!!.copyFromRealm(results)))
            list = ArrayList(realm!!.copyFromRealm(results))


            adapter = ListAdapter(list, this, context!!)
            list_recycle.layoutManager = LinearLayoutManager(activity)
            list_recycle.adapter = adapter
            adapter!!.notifyDataSetChanged()

        } finally {
            realm!!.close()
        }
        return list
    }




}

package com.example.likelab.List

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.likelab.realm.TableUserProfile
import kotlinx.android.synthetic.main.item_userprofile.view.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Log.e
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.likelab.R

import java.io.File


class ListAdapter(
    var list: ArrayList<TableUserProfile>, private var listener: Listener,
    var context: Context
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_userprofile, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }
    fun updateView(resUpdate: ArrayList<TableUserProfile>?) {
        list = resUpdate!!
        notifyDataSetChanged()
    }
    interface Listener {
        fun onItemClickDelete(tableUserProfile: TableUserProfile, position: Int)
        fun onItemClickEdit(tableUserProfile: TableUserProfile, byteArray: String)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener, position)


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(list: TableUserProfile, listener: Listener, position: Int) {

            itemView.textViewNameList.text = "${position + 1}.Name : ${list.name}"
            itemView.textViewDateTimeList.text = "${list.date} ${list.time}"
            Glide.with(context).load(list.image)
                .apply(
                    RequestOptions()
                        .centerCrop())
                .into(itemView.imageViewProfile)
            itemView.buttonDelete.setOnClickListener {
                listener.onItemClickDelete(list, position)
                notifyDataSetChanged()
            }
            itemView.buttonEdit.setOnClickListener {
                listener.onItemClickEdit(list, list.image.toString())
                notifyDataSetChanged()
            }
        }
    }

}
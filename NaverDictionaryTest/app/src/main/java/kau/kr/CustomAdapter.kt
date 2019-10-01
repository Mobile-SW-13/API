package kau.kr


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_view_item.view.*


/**
 * Created by js on 2018-02-11.
 */

class CustomAdapter(val homefeed: Homefeed) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    //아이템의 갯수
    override fun getItemCount(): Int {
        println(homefeed.items.count())
        return homefeed.items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(homefeed.items.get(position))
    }


    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(data : Item){
            //

            itemView.title.text = data.title
            itemView.description.text = "뜻 :  ${data.description}"

        }
    }

}
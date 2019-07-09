package com.example.yabamiru

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.main_list_row.view.*

class RecyclerAdapter(
    private val context: Context, private val itemClickListener: RecyclerViewHolder.ItemClickListener,
    private val itemTitles: List<String>, private val itemDeadlines: List<String>,
    private val itemPercents: List<String>, private val itemTags: List<TagRecyclerAdapter>
) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {
    //ViewのonClick時に、タップされたViewがRecyclerViewの何番目にあたるかを取得して返します

    private var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.let {
            it.itemImageView.setImageResource(R.drawable.dokuro_blue)
            it.itemTitleView.text = itemTitles.get(position)
            it.itemDeadlineView.text = itemDeadlines.get(position)
            it.itemPercent.text = itemPercents.get(position)
            it.itemTags.adapter = itemTags.get(position)
            it.itemTags.layoutManager =
                LinearLayoutManager(holder.itemTags.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun getItemCount(): Int {
        return itemTitles.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.main_list_row, parent, false)

        mView.setOnClickListener { view ->
            mRecyclerView?.let {
                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
        }
        return RecyclerViewHolder(mView)
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        interface ItemClickListener {
            fun onItemClick(view: View, position: Int)
        }


        val itemImageView: ImageView = view.main_row_imageView
        val itemTitleView: TextView = view.row_title
        val itemDeadlineView: TextView = view.row_deadline
        val itemPercent: TextView = view.row_percent
        val itemTags: RecyclerView = view.row_tag_listview

        init {
            //layoutの初期設定をする
        }
    }

}
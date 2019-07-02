package com.example.yabamiru

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.list_item.view.*

class ContactArrayAdapter(context: Context, contacts: List<Contact>): ArrayAdapter<Contact>(context, 0, contacts){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
        val rootView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val currentContact = getItem(position)

        rootView.contactImage.setImageResource(currentContact.imageResource)
        rootView.contactName.text = currentContact.name
        rootView.contactDescription.text = currentContact.description

        return rootView
    }
}
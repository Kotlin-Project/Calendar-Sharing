package com.example.kotlincalendar

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

class CustomSpinnerAdapter(context: Context, resource: Int, items: List<String>) :
    ArrayAdapter<String>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        // 사용자 정의 폰트 적용
        val typeface = ResourcesCompat.getFont(context, R.font.a1009_2)
        (view as TextView).typeface = typeface
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        // 사용자 정의 폰트 적용
        val typeface = ResourcesCompat.getFont(context, R.font.a1009_2)
        (view as TextView).typeface = typeface
        return view
    }
}
package com.example.kotlincalendar.ShareCalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.kotlincalendar.Entity.ShareCalendar
import com.example.kotlincalendar.R
import com.example.kotlincalendar.database.AppDatabase
import com.example.kotlincalendar.databinding.ActivityShareCalendarEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShareCalendarEdit : AppCompatActivity() {
    private var mBinding:ActivityShareCalendarEditBinding?=null
    private val binding get()=mBinding!!
    private val db:AppDatabase=AppDatabase.getInstance(this@ShareCalendarEdit)!!
    private val categoryList = arrayOf("친구", "업무", "연인", "가족").toList()
    private var imageResource:Int=0
    private var shareCalendarIdEdit: String = ""
    private var userEmail:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityShareCalendarEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shareCalendarIdEdit = intent.getStringExtra("shareCalendarId")!!
        userEmail=intent.getStringExtra("userEmail")!!

        editInitializeViews()

        binding.shareCalendarEditBtnEdit.setOnClickListener {
            editBtnClick()
        }
        binding.shareCalendarCancelBtnEdit.setOnClickListener {
            editCancelBtnClick()
        }
    }
    private fun editInitializeViews(){
        setupSpinners()
        GlobalScope.launch(Dispatchers.IO){
            val editShareCalendar=db!!.shareCalendarDao().getShareCalendar(shareCalendarIdEdit)
            withContext(Dispatchers.Main){
                imageResource = editShareCalendar.shareCalendarImage
                binding.shareCalendarTitleEdit.setText(editShareCalendar.shareCalendarTitle)
                binding.shareCalendarSubTitleEdit.setText(editShareCalendar.shareCalendarSubTitle)
                binding.shareCalendarImageEdit.setImageResource(imageResource)
                setupCategorySpinner(editShareCalendar.shareCalendarCategory)
            }
        }

    }
    private fun setupSpinners(){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.shareCalendarCategoryEdit.adapter = adapter
    }
    private fun setupCategorySpinner(selectedCategory: String) {
        val indexOfSelectedCategory = categoryList.indexOf(selectedCategory)
        binding.shareCalendarCategoryEdit.setSelection(indexOfSelectedCategory)
    }
    private fun editBtnClick(){
        GlobalScope.launch(Dispatchers.IO){
            val editShareCalendar=db!!.shareCalendarDao().getShareCalendar(shareCalendarIdEdit)
            editShareCalendar.shareCalendarTitle=binding.shareCalendarTitleEdit.getText().toString()
            editShareCalendar.shareCalendarSubTitle=binding.shareCalendarSubTitleEdit.getText().toString()
            editShareCalendar.shareCalendarCategory=binding.shareCalendarCategoryEdit.selectedItem.toString()
            db!!.shareCalendarDao().updateShareCalendar(editShareCalendar)
        }
    }
    private fun editCancelBtnClick(){
        finish()
    }
}

package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.ViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var viewModel: AddCourseViewModel
    private lateinit var addEdCourseName: TextInputEditText
    private lateinit var addEdLecturer: TextInputEditText
    private lateinit var addEdNote: TextInputEditText
    private lateinit var addTvStartTime: TextView
    private lateinit var addTvEndTime: TextView
    private lateinit var addBtnStartTime: ImageButton
    private lateinit var addBtnEndTime: ImageButton
    private lateinit var addSpnDay: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)

        addEdCourseName = findViewById(R.id.add_ed_course_name)
        addEdLecturer = findViewById(R.id.add_ed_lecturer)
        addEdNote = findViewById(R.id.add_ed_note)
        addTvStartTime = findViewById(R.id.add_tv_start_time)
        addTvEndTime = findViewById(R.id.add_tv_end_time)
        addBtnStartTime = findViewById(R.id.add_btn_start_time)
        addBtnEndTime = findViewById(R.id.add_btn_end_time)
        addSpnDay = findViewById(R.id.add_spn_day)

        addBtnStartTime.setOnClickListener {
            showStartTimeDialog()
        }
        addBtnEndTime.setOnClickListener {
            showEndTimeDialog()
        }

        viewModel.saved.observe(this){
            if (it.eventHandled){
                Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_insert -> {
                if (addEdCourseName.text.toString() == "" ||
                    addTvStartTime.text.toString() == "" ||
                    addTvEndTime.text.toString() == "" ||
                    addEdLecturer.text.toString() == "" ||
                    addEdNote.text.toString() == ""
                ) Toast.makeText(this, "Please fill all fields first!", Toast.LENGTH_SHORT).show()
                else {
                    viewModel.insertCourse(
                        addEdCourseName.text.toString(),
                        addSpnDay.selectedItemPosition,
                        addTvStartTime.text.toString(),
                        addTvEndTime.text.toString(),
                        addEdLecturer.text.toString(),
                        addEdNote.text.toString()
                    )
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showStartTimeDialog(){
        TimePickerFragment().show(supportFragmentManager, "start")
    }

    private fun showEndTimeDialog(){
        TimePickerFragment().show(supportFragmentManager, "end")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val strHour = when {
           hour < 10 -> "0${hour}"
            else -> hour.toString()
        }
        val strMinute = when {
            minute < 10 -> "0${minute}"
            else -> minute.toString()
        }
        val time = "${strHour}:${strMinute}"
        if (tag == "start"){
            addTvStartTime.text = time
        }
        else{
            addTvEndTime.text = time
        }
    }
}
package com.example.studentsapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Student(var id: String, var name: String, var isChecked: Boolean = false)

object StudentDatabase {
    val students = mutableListOf<Student>()
}

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd: Button = findViewById(R.id.btnAddStudent)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(StudentDatabase.students, this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddStudentActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}

class StudentAdapter(private val students: List<Student>, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgStudent: ImageView = itemView.findViewById(R.id.imgStudent)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtId: TextView = itemView.findViewById(R.id.txtId)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.txtName.text = student.name
        holder.txtId.text = student.id
        holder.checkBox.isChecked = student.isChecked

        holder.imgStudent.setImageResource(R.drawable.student_avatar)

        holder.checkBox.setOnClickListener { student.isChecked = !student.isChecked }
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, StudentDetailsActivity::class.java)
            intent.putExtra("studentIndex", position)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = students.size
}

class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val etName: EditText = findViewById(R.id.etName)
        val etId: EditText = findViewById(R.id.etId)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val id = etId.text.toString()
            if (name.isNotEmpty() && id.isNotEmpty()) {
                StudentDatabase.students.add(Student(id, name))
                finish()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}

class StudentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        val index = intent.getIntExtra("studentIndex", -1)
        if (index == -1) return

        val student = StudentDatabase.students[index]
        val txtName: TextView = findViewById(R.id.txtNameDetail)
        val txtId: TextView = findViewById(R.id.txtIdDetail)
        val btnEdit: Button = findViewById(R.id.btnEdit)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        txtName.text = student.name
        txtId.text = student.id

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java)
            intent.putExtra("studentIndex", index)
            startActivity(intent)
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}

class EditStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        val index = intent.getIntExtra("studentIndex", -1)
        if (index == -1) return

        val student = StudentDatabase.students[index]
        val etName: EditText = findViewById(R.id.etEditName)
        val etId: EditText = findViewById(R.id.etEditId)
        val btnUpdate: Button = findViewById(R.id.btnUpdate)
        val btnDelete: Button = findViewById(R.id.btnDelete)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        etName.setText(student.name)
        etId.setText(student.id)

        btnUpdate.setOnClickListener {
            student.name = etName.text.toString()
            student.id = etId.text.toString()
            goToMainActivity()
        }

        btnDelete.setOnClickListener {
            StudentDatabase.students.removeAt(index)
            goToMainActivity()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}



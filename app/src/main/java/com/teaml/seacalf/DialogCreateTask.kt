package com.teaml.seacalf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.teaml.seacalf.PreferencesManager.Task

class DialogCreateTask : DialogFragment() {
    private lateinit var preferencesManager: PreferencesManager

    interface OnTaskCreatedListener {
        fun onTaskCreated(id: Int)
    }

    private var listener: OnTaskCreatedListener? = null

    fun setListener(listener: OnTaskCreatedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_create_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager.getInstance(requireContext())

        val taskNameInput = view.findViewById<EditText>(R.id.dialog_edit_text_name)
        val taskDaysInput = view.findViewById<EditText>(R.id.dialog_edit_text_days)
        val taskDaysText = view.findViewById<TextView>(R.id.dialog_text_days)
        val negativeButton = view.findViewById<Button>(R.id.negative_button)
        val positiveButton = view.findViewById<Button>(R.id.positive_button)

        fun onTaskCreatedListener() {
            val task = Task()

            task.name = taskNameInput.text.toString()
            task.progress = 0
            task.max = taskDaysInput.text.toString().toIntOrNull() ?: 0

            if(task.name.trim().isEmpty()) {
                taskNameInput.error = "Название цели не может быть пустым"
                taskNameInput.requestFocus()
            }

            else if(task.max == 0) {
                taskDaysInput.error = "Количество дней не может быть пустым"
                taskDaysInput.requestFocus()
            }

            else {
                preferencesManager.saveTaskData(task)
                listener?.onTaskCreated(preferencesManager.getTasksNumber()-1)
                dismiss()
            }
        }

        taskDaysText.setOnClickListener {
            taskDaysInput.requestFocus()
        }

        negativeButton.setOnClickListener {
            dismiss()
        }

        positiveButton.setOnClickListener {
            onTaskCreatedListener()
        }
    }

    companion object {
        fun newInstance() = DialogCreateTask()
    }
}
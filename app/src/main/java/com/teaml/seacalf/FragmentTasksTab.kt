package com.teaml.seacalf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.teaml.seacalf.DialogCreateTask


class TasksTabFragment : Fragment(), DialogCreateTask.OnTaskCreatedListener {
    private lateinit var preferencesManager: PreferencesManager
    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tasks_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        preferencesManager = PreferencesManager.getInstance(requireContext())

        loadTasksData(view)

        val addTaskButton = view.findViewById<Button>(R.id.add_task_button)
        addTaskButton.setOnClickListener {
            showDialogCreateTask()
        }
    }

    fun showDialogCreateTask() {
        val dialog = DialogCreateTask.newInstance()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "DialogCreateTask")
    }

    fun loadTasksData(view: View) {
        val tasksNumber = preferencesManager.getTasksNumber()
        if (tasksNumber == 0)
            return
        val tasks = preferencesManager.getTasks()

        val inflater = LayoutInflater.from(context)
        val container = view.findViewById<LinearLayout>(R.id.tasks_container)

        for(i in 0..<tasksNumber) {
            val sampleTask = inflater.inflate(R.layout.sample_task, container, false)
            val taskName = sampleTask.findViewById<TextView>(R.id.sample_task_name)
            val taskProgressBar = sampleTask.findViewById<ProgressBar>(R.id.sample_task_pb)
            taskName.text = tasks[i].name
            taskProgressBar.progress = tasks[i].progress
            taskProgressBar.max = tasks[i].max

            container.addView(sampleTask)
        }
    }

    override fun onTaskCreated(id: Int) {
        val task = preferencesManager.getTaskById(id)

        val inflater = LayoutInflater.from(context)
        val container = rootView?.findViewById<LinearLayout>(R.id.tasks_container)
            ?: return
        val sampleTask = inflater.inflate(R.layout.sample_task, container, false)
        val taskName = sampleTask.findViewById<TextView>(R.id.sample_task_name)
        val taskProgressBar = sampleTask.findViewById<ProgressBar>(R.id.sample_task_pb)

        taskName.text = task.name
        taskProgressBar.progress = task.progress
        taskProgressBar.max = task.max

        container.addView(sampleTask)
        val taskCreatedMessage = Toast.makeText(
            requireContext(),
            "Создано!",
            Toast.LENGTH_SHORT
        )
        taskCreatedMessage.show()
    }
}
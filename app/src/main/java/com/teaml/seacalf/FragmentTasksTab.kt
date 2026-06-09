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

    private fun setupTaskView(sampleTask: View, index: Int) {
        val task = preferencesManager.getTaskById(index)

        val taskName = sampleTask.findViewById<TextView>(R.id.sample_task_name)
        val progressBar = sampleTask.findViewById<ProgressBar>(R.id.sample_task_pb)
        val btnMinus = sampleTask.findViewById<Button>(R.id.sample_task_minus)
        val btnPlus = sampleTask.findViewById<Button>(R.id.sample_task_plus)

        taskName.text = task.name
        progressBar.progress = task.progress
        progressBar.max = task.max

        sampleTask.tag = index

        btnPlus.setOnClickListener {
            updateTaskProgress(index, 1)
        }

        btnMinus.setOnClickListener {
            deleteTask(index)
        }
    }

    private fun updateTaskProgress(taskIndex: Int, delta: Int) {
        val task = preferencesManager.getTaskById(taskIndex)
        val oldProgress = task.progress

        task.progress = (task.progress + delta).coerceIn(0, task.max)

        preferencesManager.saveTaskProgress(taskIndex, task.progress)

        if (delta > 0) {
            preferencesManager.addPetProgress(1)
        }

        if (oldProgress < task.max && task.progress >= task.max) {
            giveTaskCompletionRewards(task)
            preferencesManager.deleteTask(taskIndex) // delete if error and add deleteTask fun
            safeLoadTasksData()
            return
        }

        refreshTaskView(taskIndex)
    }

    private fun giveTaskCompletionRewards(task: PreferencesManager.Task) {
        val expBonus = task.max / 3

        preferencesManager.addPetProgress(expBonus)

        val currentCoins = preferencesManager.getCoinsAmount()
        preferencesManager.setCoinsAmount(currentCoins + task.max)

        (requireActivity() as? MainActivity)?.refreshHeader()

        val tasksCompleted = preferencesManager.getStatAt(1) + 1
        val coinsEarned = preferencesManager.getStatAt(4) + task.max
        preferencesManager.setStatAt(1, tasksCompleted)
        preferencesManager.setStatAt(4, coinsEarned)

        Toast.makeText(
            requireContext(),
            "Задача выполнена!\n+${task.max} монет и +$expBonus опыта",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun deleteTask(index: Int) {
        preferencesManager.deleteTask(index)
        val tasksFailed = preferencesManager.getStatAt(2) + 1
        preferencesManager.setStatAt(2, tasksFailed)

        safeLoadTasksData()

        Toast.makeText(requireContext(), "Задача удалена", Toast.LENGTH_SHORT).show()
    }

    private fun refreshTaskView(index: Int) {
        val container = rootView?.findViewById<LinearLayout>(R.id.tasks_container) ?: return

        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child.tag == index) {
                val progressBar = child.findViewById<ProgressBar>(R.id.sample_task_pb)
                val task = preferencesManager.getTaskById(index)
                progressBar.progress = task.progress
                break
            }
        }
    }

    fun loadTasksData(view: View) {
        val tasksNumber = preferencesManager.getTasksNumber()
        val container = view.findViewById<LinearLayout>(R.id.tasks_container)

        container.removeAllViews()

        if (tasksNumber == 0) return

        val inflater = LayoutInflater.from(context)

        for (i in 0 until tasksNumber) {
            val sampleTask = inflater.inflate(R.layout.sample_task, container, false)
            setupTaskView(sampleTask, i)
            container.addView(sampleTask)
        }
    }

    private fun safeLoadTasksData() {
        rootView?.let { loadTasksData(it) }
    }

    override fun onTaskCreated(id: Int) {
        loadTasksData(rootView ?: return)
        Toast.makeText(requireContext(), "Создано!", Toast.LENGTH_SHORT).show()
    }
}
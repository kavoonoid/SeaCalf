package com.teaml.seacalf
import android.content.Context
import androidx.core.content.edit

/* general
    |--coinsAmount
    |--tasksInProcess
    |--petName
    |--petLevel
    |--petProgress
    |--petMax
    |--petSelectedSkin
    |--petSelectedHat

   tasks
    |--taskName1
    |--taskProgress1
    |--taskMax1
    |
    ...
    |
    |--taskNameN
    |--taskProgressN
    |--taskMaxN

   stats
    |--statValue1
    |
    ...
    |
    |statValueN

   items
    |--itemName1
    |--itemStatus1
    |
    ...
    |
    |--itemNameN
    |--itemStatusN
*/

class PreferencesManager(context: Context) {
    private val generalPreferences = context.getSharedPreferences(GENERAL_PREFS, Context.MODE_PRIVATE)
    private val taskPreferences = context.getSharedPreferences(TASK_PREFS, Context.MODE_PRIVATE)
    private val statPreferences = context.getSharedPreferences(STAT_PREFS, Context.MODE_PRIVATE)
    private val itemPreferences = context.getSharedPreferences(STAT_PREFS, Context.MODE_PRIVATE)

    class Task {
        var name: String = "NAME"
        var progress: Int = 0
        var max: Int = 0
        var id: Int = 0
    }

    // FOR GENERAL

    fun getCoinsAmount(): Int {
        return generalPreferences.getInt("coins_amount", 0)
    }

    fun getTasksNumber(): Int {
        return generalPreferences.getInt("tasks_in_process", 0)
    }

    fun setTasksNumber(num: Int) {
        generalPreferences.edit {
            putInt("tasks_in_process", num)
            apply()
        }
    }

    fun getPetName(): String {
        return generalPreferences.getString("pet_name", "NAME").toString()
    }

    fun setPetName(name: String) {
        generalPreferences.edit {
            putString("pet_name", name)
            apply()
        }
    }

    fun getPetLevel(): Int {
        return generalPreferences.getInt("pet_level", 0)
    }

    fun setPetLevel(level: Int) {
        generalPreferences.edit {
            putInt("pet_level", level)
        }
    }

    fun getPetProgress(): Int {
        return generalPreferences.getInt("pet_progress", 0)
    }

    fun setPetProgress(progress: Int) {
        generalPreferences.edit {
            putInt("pet_progress", progress)
        }
    }


    fun getPetMax(): Int {
        return generalPreferences.getInt("pet_max", 1)
    }

    fun setPetMax(max: Int) {
        generalPreferences.edit {
            putInt("pet_max", max)
        }
    }
    // FOR TASKS
    fun getTasks(): Array<Task> {
        val tasksInProcess = getTasksNumber()
        val tasks = Array(tasksInProcess, {Task()})
        for(i in 0..<tasksInProcess) {
            tasks[i].name = taskPreferences.getString("task_name_$i", "name").toString()
            tasks[i].progress = taskPreferences.getInt("task_progress_$i", 0)
            tasks[i].max = taskPreferences.getInt("task_max_$i", 1)
            tasks[i].id = i
        }

        return tasks
    }

    fun getTaskById(id: Int): Task {
        val task = Task()
        task.name = taskPreferences.getString("task_name_$id", "name").toString()
        task.progress = taskPreferences.getInt("task_progress_$id", 0)
        task.max = taskPreferences.getInt("task_max_$id", 1)
        task.id = id
        return task
    }

    fun saveTaskProgressById(id: Int, progress: Int) {
        taskPreferences.edit {
            putInt("task_progress_$id", progress)
            apply()
        }
    }

    fun saveTaskData(task: Task) {
        val tasksNumber = getTasksNumber()
        setTasksNumber(tasksNumber+1)

        taskPreferences.edit {
            putString("task_name_$tasksNumber", task.name)
            putInt("task_progress_$tasksNumber", task.progress)
            putInt("task_max_$tasksNumber", task.max)
            apply()
        }
    }

    fun onTaskDeleted() {}
    fun onTaskCompleted() {}

    // FOR STATS
    fun loadStats(): Array<Int> {
        val stats = Array(5, {0})
        for(i in 0..4) {
            stats[i] = statPreferences.getInt("stat_value_$i", 0)
        }

        return stats
    }

    // GLOBAL VISIBLE
    companion object {
        private const val GENERAL_PREFS = "general_prefs"
        private const val TASK_PREFS = "task_prefs"
        private const val STAT_PREFS = "stat_prefs"
        private const val ITEM_PREFS = "item_prefs"

        @Volatile
        private var instance: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: PreferencesManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}
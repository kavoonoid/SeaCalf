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
    |--itemState1 [own] | [none]
    |
    ...
    |
    |--itemStateN
*/

class PreferencesManager(context: Context) {
    private val generalPreferences = context.getSharedPreferences(GENERAL_PREFS, Context.MODE_PRIVATE)
    private val taskPreferences = context.getSharedPreferences(TASK_PREFS, Context.MODE_PRIVATE)
    private val statPreferences = context.getSharedPreferences(STAT_PREFS, Context.MODE_PRIVATE)
    private val itemPreferences = context.getSharedPreferences(ITEM_PREFS, Context.MODE_PRIVATE)

    class Task {
        var name: String = "NAME"
        var progress: Int = 0
        var max: Int = 0
    }

    // FOR GENERAL

    fun getCoinsAmount(): Int {
        return generalPreferences.getInt("coins_amount", 0)
    }

    fun setCoinsAmount(num: Int) {
        generalPreferences.edit {
            putInt("coins_amount", num)
            apply()
        }
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
        return generalPreferences.getString("pet_name", "Антон").toString()
    }

    fun setPetName(name: String) {
        generalPreferences.edit {
            putString("pet_name", name)
            apply()
        }
    }

    fun getPetLevel(): Int {
        return generalPreferences.getInt("pet_level", 1)
    }

    fun setPetLevel(level: Int) {
        generalPreferences.edit {
            putInt("pet_level", level)
            apply()
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
        return generalPreferences.getInt("pet_max", 5)
    }

    fun setPetMax(max: Int) {
        generalPreferences.edit {
            putInt("pet_max", max)
            apply()
        }
    }

    fun addPetProgress(amount: Int) {
        if (amount <= 0) return

        var currentProgress = getPetProgress()
        var currentLevel = getPetLevel()
        var currentMax = getPetMax()

        currentProgress += amount

        while (currentProgress >= currentMax && currentMax > 0) {
            currentProgress -= currentMax
            currentLevel++
            currentMax = currentLevel * 5
        }

        setPetLevel(currentLevel)
        setPetProgress(currentProgress)
        setPetMax(currentMax)
        setStatAt(3, getPetLevel())
    }

    fun getSelectedHat(): Int {
        return generalPreferences.getInt("pet_selected_hat", 0)
    }

    fun setSelectedHat(hatId: Int) {
        generalPreferences.edit {
            putInt("pet_selected_hat", hatId)
            apply()
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
        }

        return tasks
    }

    fun getTaskById(id: Int): Task {
        val task = Task()
        task.name = taskPreferences.getString("task_name_$id", "name").toString()
        task.progress = taskPreferences.getInt("task_progress_$id", 0)
        task.max = taskPreferences.getInt("task_max_$id", 1)
        return task
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

    fun saveTaskProgress(id: Int, newProgress: Int) {
        taskPreferences.edit {
            putInt("task_progress_$id", newProgress)
            apply()
        }
    }

    fun deleteTask(taskId: Int) {
        val tasksNumber = getTasksNumber()
        if (taskId < 0 || taskId >= tasksNumber) return

        val editor = taskPreferences.edit()

        for (i in taskId until tasksNumber - 1) {
            val nextName = taskPreferences.getString("task_name_${i + 1}", "")
            val nextProgress = taskPreferences.getInt("task_progress_${i + 1}", 0)
            val nextMax = taskPreferences.getInt("task_max_${i + 1}", 1)

            editor.putString("task_name_$i", nextName)
            editor.putInt("task_progress_$i", nextProgress)
            editor.putInt("task_max_$i", nextMax)
        }

        editor.remove("task_name_${tasksNumber - 1}")
        editor.remove("task_progress_${tasksNumber - 1}")
        editor.remove("task_max_${tasksNumber - 1}")

        editor.apply()

        setTasksNumber(tasksNumber - 1)
    }

    // FOR STATS
    fun loadStats(): Array<Int> {
        val stats = Array(5, {0})
        for(i in 0..4) {
            stats[i] = statPreferences.getInt("stat_value_${i+1}", 0)
        }

        return stats
    }

    fun getStatAt(id: Int): Int {
        return statPreferences.getInt("stat_value_$id", 0)
    }

    fun setStatAt(id: Int, value: Int) {
        statPreferences.edit {
            putInt("stat_value_$id", value)
            apply()
        }
    }

    // FOR ITEMS
    fun getItemStateAt(id: Int): String? {
        return itemPreferences.getString("item_state_$id", "none")
    }

    fun setItemStateAt(id: Int, state: String) {
        itemPreferences.edit {
            putString("item_state_$id", state)
            apply()
        }
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
package com.teaml.seacalf

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.view.inputmethod.EditorInfo
import com.teaml.seacalf.DialogInventory
import com.teaml.seacalf.DialogShop
import com.teaml.seacalf.PreferencesManager
class FragmentPetTab : Fragment() {
    private lateinit var progressBar: CircularProgressBar
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pet_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager.getInstance(requireContext())

        // pet information
        val petName = view.findViewById<EditText>(R.id.pet_name)
        petName.setText(preferencesManager.getPetName())

        petName.setOnEditorActionListener { _, actionId, _ ->
            onPetNameUpdated(petName)
        }

        // level pb attributes
        progressBar = view.findViewById(R.id.pet_level_pb)
        val petProgress = preferencesManager.getPetProgress()
        val petMax = preferencesManager.getPetMax()
        animateProgress(petProgress, petMax)

        val pbLevelText = view.findViewById<TextView>(R.id.pb_level_text)
        val currentLevel = preferencesManager.getPetLevel()
        pbLevelText.text = getString(R.string.level_pb_text, currentLevel)

        // shop & inventory
        val buttonInventoryOpen = view.findViewById<Button>(R.id.button_inventory_open)
        val buttonShopOpen = view.findViewById<Button>(R.id.button_shop_open)

        buttonInventoryOpen.setOnClickListener {
            showDialogInventory()
        }
        buttonShopOpen.setOnClickListener {
            showDialogShop()
        }
    }

    // progress bar rendering
    private fun animateProgress(progress: Int, max: Int) {
        val animator = ValueAnimator.ofInt(progress, max).apply {
            duration = 1000L
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                progressBar.setProgress(value)
            }
        }
        animator.start()
    }

    fun showDialogInventory() {
        val dialog1 = DialogInventory.newInstance()
        dialog1.show(parentFragmentManager, "DialogInventory")
    }

    fun showDialogShop() {
        val dialog2 = DialogShop.newInstance()
        dialog2.show(parentFragmentManager, "DialogShop")

    }

    fun onPetNameUpdated(petNameEditText: EditText): Boolean {
        val name = petNameEditText.text.toString()
        if(name.trim().isEmpty()) {
            petNameEditText.error = "Имя питомца не может быть пустым"
            petNameEditText.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            return false
        }
        else {
            preferencesManager.setPetName(name)
            return true
        }
    }
}
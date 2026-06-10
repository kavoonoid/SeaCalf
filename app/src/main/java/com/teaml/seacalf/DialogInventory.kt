package com.teaml.seacalf

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class DialogInventory : DialogFragment() {
    private lateinit var preferencesManager: PreferencesManager
    override fun getTheme(): Int = R.style.DialogCustom

    interface OnSkinSelectedListener {
        fun setSkin()
    }

    private var listener: OnSkinSelectedListener? = null

    fun setListener(listener: OnSkinSelectedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        preferencesManager = PreferencesManager.getInstance(context)

        val buttonInventoryClose = view.findViewById<Button>(R.id.button_inventory_close)
        loadOwnedHats(view, context)

        buttonInventoryClose.setOnClickListener {
            dismiss()
        }
    }

    fun loadOwnedHats(view: View, context: Context) {
        val inflater = LayoutInflater.from(context)
        val inventoryContainer = view.findViewById<LinearLayout>(R.id.inventory_container)
        inventoryContainer?.removeAllViews()

        for(i in 0..3) {
            val hat = inflater.inflate(R.layout.sample_inventory_hat, inventoryContainer, false)
            val hatImage = hat.findViewById<ImageView>(R.id.inventory_hat_image)
            val hatName = hat.findViewById<TextView>(R.id.inventory_hat_name)
            val hatSelected = hat.findViewById<TextView>(R.id.inventory_hat_selected)

            val hatIconId = resources.getIdentifier("icon_hat_$i", "drawable", context.packageName)
            hatImage.setImageResource(hatIconId)
            val hatNameId = resources.getIdentifier("hat_name_$i", "string", context.packageName)
            hatName.text = resources.getString(hatNameId)

            if(preferencesManager.getSelectedHat() == i) {
                hatSelected.text = "выбрано"
            }
            if(preferencesManager.getItemStateAt(i) == "own" || i == 0) {
                inventoryContainer.addView(hat)
                hat.setOnClickListener {
                    onHatClickListener(i)
                }
            }
        }
    }

    fun onHatClickListener(id: Int) {
        preferencesManager.setSelectedHat(id)
        listener?.setSkin()
        loadOwnedHats(requireView(), requireContext())
    }

    companion object {
        fun newInstance() = DialogInventory()
    }
}
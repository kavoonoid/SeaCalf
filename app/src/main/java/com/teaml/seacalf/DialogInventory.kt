package com.teaml.seacalf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class DialogInventory : DialogFragment() {
    override fun getTheme(): Int = R.style.DialogCustom

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonInventoryClose = view.findViewById<Button>(R.id.button_inventory_close)

        buttonInventoryClose.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance() = DialogInventory()
    }
}
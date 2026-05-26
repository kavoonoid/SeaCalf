package com.teaml.seacalf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class DialogShop : DialogFragment() {
    private lateinit var preferencesManager: PreferencesManager
    override fun getTheme(): Int = R.style.DialogCustom

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager.getInstance(requireContext())

        val coinsAmountShop = view.findViewById<TextView>(R.id.coins_amount_shop)
        coinsAmountShop.text = preferencesManager.getCoinsAmount().toString()

        val buttonShopClose = view.findViewById<Button>(R.id.button_shop_close)

        buttonShopClose.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance() = DialogShop()
    }
}
package com.teaml.seacalf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
        val context = requireContext()
        preferencesManager = PreferencesManager.getInstance(requireContext())

        val coinsAmountShop = view.findViewById<TextView>(R.id.coins_amount_shop)
        coinsAmountShop.text = preferencesManager.getCoinsAmount().toString()

        val inflater = LayoutInflater.from(context)
        val shopContainer = view.findViewById<LinearLayout>(R.id.shop_container)

        for(i in 1..3) {
            val hat = inflater.inflate(R.layout.sample_shop_hat, shopContainer, false)
            val hatImage = hat.findViewById<ImageView>(R.id.shop_hat_image)
            val hatPrice = hat.findViewById<TextView>(R.id.hat_price)

            val resourceName = "icon_hat_$i"
            val resId = resources.getIdentifier(resourceName, "drawable", context.packageName)
            hatImage.setImageResource(resId)

            hatPrice.text = preferencesManager.getItemPriceById(i).toString()

            shopContainer.addView(hat)
        }

        val buttonShopClose = view.findViewById<Button>(R.id.button_shop_close)

        buttonShopClose.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance() = DialogShop()
    }
}
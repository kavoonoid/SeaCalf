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
import android.widget.Toast
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

        loadHatsData(view, context)

        val buttonShopClose = view.findViewById<Button>(R.id.button_shop_close)

        buttonShopClose.setOnClickListener {
            dismiss()
        }
    }

    fun loadHatsData(view: View, context: Context) {
        val inflater = LayoutInflater.from(context)
        val shopContainer = view.findViewById<LinearLayout>(R.id.shop_container)
        shopContainer?.removeAllViews()

        for(i in 1..3) {
            val hat = inflater.inflate(R.layout.sample_shop_hat, shopContainer, false)
            val hatImage = hat.findViewById<ImageView>(R.id.shop_hat_image)
            val hatName = hat.findViewById<TextView>(R.id.shop_hat_name)
            val hatPrice = hat.findViewById<TextView>(R.id.shop_hat_price)

            val hatIconId = resources.getIdentifier("icon_hat_$i", "drawable", context.packageName)
            hatImage.setImageResource(hatIconId)
            val hatCostId = resources.getIdentifier("hat_cost_$i", "integer", context.packageName)
            hatPrice.text = resources.getInteger(hatCostId).toString()
            val hatNameId = resources.getIdentifier("hat_name_$i", "string", context.packageName)
            hatName.text = resources.getString(hatNameId)

            if(preferencesManager.getItemStateAt(i) == "own") {
                hatPrice.text = "куплено"
            }

            hat.setOnClickListener { view ->
                onHatClickListener(view, i)
            }
            shopContainer.addView(hat)
        }
    }

    fun onHatClickListener(view: View, id: Int) {
        val priceView = view.findViewById<TextView>(R.id.shop_hat_price)
        val price = priceView.text.toString()
        val coins = preferencesManager.getCoinsAmount()
        if(price == "куплено")
            return
        if(coins < price.toInt()) {
            Toast.makeText(
                requireContext(),
                "Не хватает монеток",
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            preferencesManager.setCoinsAmount(coins - price.toInt())
            preferencesManager.setItemStateAt(id, "own")
            preferencesManager.setStatAt(5, preferencesManager.getStatAt(5)+1)
            loadHatsData(requireView(), requireContext())
            (requireActivity() as? MainActivity)?.refreshHeader()
            Toast.makeText(
                requireContext(),
                "Куплено",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun coinsUpdate(view: View) {
        val coinsAmountShop = view.findViewById<TextView>(R.id.coins_amount_shop)
        coinsAmountShop.text = preferencesManager.getCoinsAmount().toString()
    }

    companion object {
        fun newInstance() = DialogShop()
    }
}
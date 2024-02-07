package cvb.com.br.vendaomie.presentation.page_list_item.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import cvb.com.br.vendaomie.R
import cvb.com.br.vendaomie.databinding.DialogItemSaleBinding
import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.util.Constants
import cvb.com.br.vendaomie.util.DialogUtil

class ItemSaleDialog(
    activity: Activity,
    private val saleId: Long,
    private val itemSale: ItemSale?
) : Dialog(activity) {

    private var onSaveEvent: ((itemSale: ItemSale) -> Unit)? = null

    private val C_DECREASE_QTD = 0
    private val C_INCREASE_QTD = 1

    private lateinit var binding: DialogItemSaleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogItemSaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)

        setupListener()

        showInformation()
    }

    private fun setupListener() {
        binding.ivDecrease.setOnClickListener { updateQuantity(C_DECREASE_QTD) }

        binding.ivIncrease.setOnClickListener { updateQuantity(C_INCREASE_QTD) }

        binding.btSave.setOnClickListener {
            save()
        }

        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun save() {
        val prod = binding.etProd.text.toString()
        val vlUnit = try {
            binding.etValueUnit.text.toString().toDouble()
        } catch (e: Exception) {
            0.00
        }
        val qtd = binding.tvQtd.text.toString().toDouble()

        val newItemSale =
            itemSale?.let {
                itemSale.copy(product = prod, unitValue = vlUnit, quantity = qtd)
            } ?: run {
                ItemSale(
                    id = 0,
                    saleId = saleId,
                    product = prod,
                    unitValue = vlUnit,
                    quantity = qtd
                )
            }

        onSaveEvent?.invoke(newItemSale)
    }

    fun setOnSaveEvent(event: (itemSale: ItemSale) -> Unit) {
        this.onSaveEvent = event
    }

    private fun updateQuantity(type: Int) {
        var currentQtd = binding.tvQtd.text.toString().toDouble()

        if (type == C_DECREASE_QTD) {
            if (currentQtd > 0) {
                currentQtd--
            }
        } else {
            if (currentQtd < Constants.LIMIT_QTD_REQUEST) {
                currentQtd++
            }
        }

        binding.tvQtd.setText(currentQtd.toString())
    }

    private fun showInformation() {
        itemSale?.let { item ->
            binding.tvTitle.text = context.getString(R.string.page_item_dialog_title_update)
            binding.etProd.visibility = View.INVISIBLE
            binding.tvProd.visibility = View.VISIBLE

            binding.etProd.setText(item.product)
            binding.tvProd.text = item.product
            binding.etValueUnit.setText(item.unitValue.toString())
            binding.tvQtd.setText(item.quantity.toString())
        }
    }

    fun showMessage(idMessage: Int) {
        val msg = context.getString(idMessage)

        val btOK = {}
        DialogUtil(context).showAlertDialog(msg, btOK)
    }
}

package cvb.com.br.vendaomie.presentation.page_list_sale.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import cvb.com.br.vendaomie.databinding.DialogInformClientBinding
import cvb.com.br.vendaomie.util.DialogUtil

class InformClientDialog(activity: Activity) : Dialog(activity) {

    private var onOKEvent: ((client: String) -> Unit)? = null

    private lateinit var binding: DialogInformClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogInformClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)

        setupListener()
    }

    private fun setupListener() {
        binding.btOk.setOnClickListener {
            save()
        }

        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }

    fun showErrorMessage(idMessage: Int) {
        val msg = context.getString(idMessage)

        val btOK = {}
        DialogUtil(context).showAlertDialog(msg, btOK)
    }

    private fun save() {
        val client = binding.etClient.text.toString()
        onOKEvent?.invoke(client)
    }

    fun setOnOkEvent(event: (client: String) -> Unit) {
        this.onOKEvent = event
    }
}

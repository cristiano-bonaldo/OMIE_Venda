package cvb.com.br.vendaomie.util

import android.app.AlertDialog
import android.content.Context
import cvb.com.br.vendaomie.R
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DialogUtil @Inject constructor(@ActivityContext private val activity: Context) {

    fun showDialog(
        title: String, message: String,
        btPositiveTitle: String,
        btPositiveEvent: (() -> Unit)?,
        btNegativeTitle: String,
        btNegativeEvent: (() -> Unit)?
    ) {

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(btPositiveTitle) { _, _ -> btPositiveEvent?.invoke() }

        builder.setNegativeButton(btNegativeTitle) { _, _ -> btNegativeEvent?.invoke() }

        builder.show()
    }

    fun showErrorDialog(errorMessage: String, btOkEvent: (() -> Unit)) {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_error)
        builder.setTitle(activity.getString(R.string.dialog_error_title))
        builder.setMessage(errorMessage)

        builder.setPositiveButton(R.string.dialog_error_bt_ok) { _, _ -> btOkEvent.invoke() }

        builder.show()
    }

    fun showAlertDialog(errorMessage: String, btOkEvent: (() -> Unit)) {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_alert)
        builder.setTitle(activity.getString(R.string.dialog_alert_title))
        builder.setMessage(errorMessage)

        builder.setPositiveButton(R.string.dialog_alert_bt_ok) { _, _ -> btOkEvent.invoke() }

        builder.show()
    }

    fun showDataShareDialog(selectOptionListener: ((Int) -> Unit), btCloseListener: (() -> Unit)) {
        val optionList = arrayOf(
            activity.getString(R.string.share_data_option_whatsapp),
            activity.getString(R.string.share_data_option_mail),
            activity.getString(R.string.share_data_option_generic)
        )

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setTitle(activity.getString(R.string.share_data_title))
        builder.setSingleChoiceItems(optionList, -1) { _, option ->
            selectOptionListener.invoke(option)
            /*
            1º parameter ('_') = dialogInterface
            dialogInterface.dismiss()
            */
        }
        builder.setPositiveButton(R.string.share_data_bt_close) { _, _ -> btCloseListener.invoke() }
        builder.show()
    }
}
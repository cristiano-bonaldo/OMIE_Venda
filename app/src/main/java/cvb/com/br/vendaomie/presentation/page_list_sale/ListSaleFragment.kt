package cvb.com.br.vendaomie.presentation.page_list_sale

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cvb.com.br.vendaomie.R
import cvb.com.br.vendaomie.databinding.FragmentListSaleBinding
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.presentation.UIStatus
import cvb.com.br.vendaomie.presentation.page_list_sale.adapter.SaleAdapter
import cvb.com.br.vendaomie.presentation.page_list_sale.dialog.InformClientDialog
import cvb.com.br.vendaomie.util.DataShareUtil
import cvb.com.br.vendaomie.util.DialogUtil
import cvb.com.br.vendaomie.util.SaleUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListSaleFragment : Fragment(R.layout.fragment_list_sale) {

    companion object {
        private val TAG = ListSaleFragment::class.java.simpleName
    }

    //------------------------

    @Inject
    lateinit var dialogUtil: DialogUtil

    @Inject
    lateinit var dataShareUtil: DataShareUtil

    @Inject
    lateinit var saleAdapter: SaleAdapter

    //------------------------

    private var informClientDialog: InformClientDialog? = null

    private var _binding: FragmentListSaleBinding? = null
    private val binding: FragmentListSaleBinding get() = _binding!!

    private val viewModel by viewModels<FragListSaleViewModel>()

    //------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListSaleBinding.bind(view)

        setupObserver()

        setupListener()

        setupRecycler()

        viewModel.handleEvent(ListSaleEvent.LoadData)
    }

    private fun setupListener() {
        binding.btNewSale.setOnClickListener { showDialogInformClient() }
    }

    private fun navigateToListItem(saleId: Long) {
        val navTo = ListSaleFragmentDirections.actionListSaleFragmentToFragmentTest(saleId)
        findNavController().navigate(navTo)
    }

    private fun setupRecycler() {
        saleAdapter.setOnShareEvent { sale -> showDialogShare(sale) }
        saleAdapter.setOnEditEvent { sale -> navigateToListItem(sale.id) }
        saleAdapter.setOnDeleteEvent { sale -> showDialogDeleteSale(sale) }

        binding.recycler.adapter = saleAdapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObserver() {
        viewModel.loadData.observe(viewLifecycleOwner, this::onLoadData)
        viewModel.insertData.observe(viewLifecycleOwner, this::onInsertData)
        viewModel.deleteData.observe(viewLifecycleOwner, this::onDeleteData)

        viewModel.isLoading.observe(viewLifecycleOwner, this::onLoadingStatus)
    }

    private fun onLoadingStatus(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) { View.VISIBLE } else { View.GONE }
    }

    private fun onLoadData(status: UIStatus<List<Sale>>) {
        when (status) {
            is UIStatus.Loading -> {
                Log.i(TAG, "onLoadData::Status=Loading")
            }

            is UIStatus.Error -> {
                val errorMessage = status.error.message ?: "-"
                Log.i(TAG, "onLoadData::Status=Error Message: $errorMessage")
                handleError(errorMessage)
            }

            is UIStatus.Success -> {
                Log.i(TAG, "onLoadData::Status=Success")
                val listSale = status.result ?: emptyList()
                refreshList(listSale)
            }
        }
    }

    private fun onDeleteData(status: UIStatus<List<Sale>>) {
        when (status) {
            is UIStatus.Loading -> {
                Log.i(TAG, "onDeleteData::Status=Loading")
            }

            is UIStatus.Error -> {
                val errorMessage = status.error.message ?: "-"
                Log.i(TAG, "onDeleteData::Status=Error Message: $errorMessage")
                handleError(errorMessage)
            }

            is UIStatus.Success -> {
                Log.i(TAG, "onDeleteData::Status=Success")
                val listSale = status.result ?: emptyList()
                refreshList(listSale)
            }
        }
    }

    private fun onInsertData(status: UIStatus<Long>) {
        when (status) {
            is UIStatus.Loading -> {
                Log.i(TAG, "onInsertData::Status=Loading")
            }

            is UIStatus.Error -> {
                val errorMessage = status.error.message ?: "-"
                Log.i(TAG, "onInsertData::Status=Error Message: $errorMessage")
                handleError(errorMessage)
            }

            is UIStatus.Success -> {
                Log.i(TAG, "onInsertData::Status=Success")

                informClientDialog?.dismiss()

                val saleId = status.result
                saleId?.let { idSale ->
                    if (viewModel.isValidNavigation(idSale)) {
                        navigateToListItem(idSale)
                    }
                }
            }
        }
    }

    private fun handleError(errorMessage: String) {
        val msg = requireActivity().getString(R.string.dialog_error_msg, errorMessage)

        val btOk = {}
        dialogUtil.showErrorDialog(msg, btOk)
    }

    private fun refreshList(list: List<Sale>) {
        val hasData = list.isNotEmpty()
        binding.vgEmptyData.visibility = if (hasData) { View.GONE } else { View.VISIBLE }

        saleAdapter.submitList(list)
    }

    private fun showDialogInformClient() {
        informClientDialog = InformClientDialog (requireActivity() )
        informClientDialog?.setOnOkEvent { client -> save(client) }
        informClientDialog?.show()
    }

    private fun save(client: String) {
        val idError = viewModel.validateClient(client)

        if (idError >= 0) {
            informClientDialog?.showErrorMessage(idError)
        } else {
            viewModel.handleEvent(ListSaleEvent.CreateSaleRecord(client))
        }
    }

    private fun showDialogDeleteSale(sale: Sale) {
        val btOk = {
            viewModel.handleEvent(ListSaleEvent.DeleteSaleRecord(sale))
        }

        dialogUtil.showDialog(
            getString(R.string.page_sale_delete_title),
            getString(R.string.page_sale_delete_msg, sale.client, sale.id.toString()),
            getString(R.string.page_sale_delete_bt_ok),
            btOk,
            getString(R.string.page_sale_delete_bt_cancel),
            null)
    }

    private fun showDialogShare(sale: Sale) {
        val saleInfo = SaleUtil.getInfoPurchaseFormatted(requireContext(), sale)

        val optionChoice: ((Int) -> Unit) = { selectedOption ->
            when (selectedOption) {
                0 -> dataShareUtil.sendMessageToWhatsApp(saleInfo)
                1 -> dataShareUtil.sendMessageToEmail(saleInfo)
                else -> dataShareUtil.shareData(saleInfo)
            }
        }
        val btClose: (() -> Unit)  = { }

        dialogUtil.showDataShareDialog(optionChoice, btClose)
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}
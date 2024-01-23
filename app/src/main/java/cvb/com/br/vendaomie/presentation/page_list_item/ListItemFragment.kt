package cvb.com.br.vendaomie.presentation.page_list_item

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cvb.com.br.vendaomie.R
import cvb.com.br.vendaomie.databinding.FragmentListItemBinding
import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.presentation.UIStatus
import cvb.com.br.vendaomie.presentation.page_list_item.adapter.ItemAdapter
import cvb.com.br.vendaomie.presentation.page_list_item.dialog.ItemSaleDialog
import cvb.com.br.vendaomie.util.DateTimeUtil
import cvb.com.br.vendaomie.util.DialogUtil
import cvb.com.br.vendaomie.util.StringUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListItemFragment : Fragment(R.layout.fragment_list_item) {

    companion object {
        private val TAG = ListItemFragment::class.java.simpleName
    }

    //------------------

    @Inject
    lateinit var dialogUtil: DialogUtil

    @Inject
    lateinit var itemAdapter: ItemAdapter

    //------------------

    private var itemSaleDialog: ItemSaleDialog? = null

    private var _binding: FragmentListItemBinding? = null
    private val binding: FragmentListItemBinding get() = _binding!!

    private val viewModel by viewModels<FragListItemViewModel>()

    private val args: ListItemFragmentArgs by navArgs()

    private var saleId: Long = -1

    //------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListItemBinding.bind(view)

        saleId = args.SALEID

        setupBackPressHandler()

        setupObserver()

        setupListener()

        setupRecycler()

        viewModel.handleEvent(ListItemEvent.LoadSale(saleId))
        viewModel.handleEvent(ListItemEvent.LoadData(saleId))
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (viewModel.saleHasNoItems()) {
                showDialogDeleteSale()
            } else {
                goBackToListSale()
            }
        }
    }

    private fun setupObserver() {
        viewModel.loadSale.observe(viewLifecycleOwner, this::onLoadSale)
        viewModel.deleteSale.observe(viewLifecycleOwner, this::onDeleteSale)

        viewModel.loadData.observe(viewLifecycleOwner, this::onLoadData)
        viewModel.insertItem.observe(viewLifecycleOwner, this::onLoadData)
        viewModel.deleteItem.observe(viewLifecycleOwner, this::onLoadData)

        viewModel.totalSale.observe(viewLifecycleOwner, this::onTotalSale)

        viewModel.isLoading.observe(viewLifecycleOwner, this::onLoadingStatus)
    }

    private fun onLoadingStatus(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) { View.VISIBLE } else { View.GONE }
    }

    private fun onTotalSale(total: Double) {
        binding.tvTotal.text = requireActivity().getString(
            R.string.page_item_total,
            StringUtil.formatValue(total)
        )
    }

    private fun onLoadSale(status: UIStatus<Sale>) {
        when (status) {
            is UIStatus.Loading -> {
                Log.i(TAG, "onLoadSale::Status=Loading")
            }

            is UIStatus.Error -> {
                val errorMessage = status.error.message ?: "-"
                Log.i(TAG, "onLoadSale::Status=Error Message: $errorMessage")
                handleError(errorMessage)
            }

            is UIStatus.Success -> {
                Log.i(TAG, "onLoadSale::Status=Success")
                val sale = status.result
                showSaleInformation(sale)
            }
        }
    }

    private fun onDeleteSale(status: UIStatus<Nothing>) {
        when (status) {
            is UIStatus.Loading -> {
                Log.i(TAG, "onDeleteSale::Status=Loading")
            }

            is UIStatus.Error -> {
                val errorMessage = status.error.message ?: "-"
                Log.i(TAG, "onDeleteSale::Status=Error Message: $errorMessage")
                handleError(errorMessage)
            }

            is UIStatus.Success -> {
                Log.i(TAG, "onDeleteSale::Status=Success")
                goBackToListSale()
            }
        }
    }

    private fun showSaleInformation(sale: Sale?) {
        sale?.let { saleInfo ->
            binding.tvClient.text = saleInfo.client
            binding.tvID.text = saleInfo.id.toString()
            binding.tvDate.text = DateTimeUtil.convertTimeMillisToString(saleInfo.date)
        }
    }

    private fun onLoadData(status: UIStatus<List<ItemSale>>) {
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
                val listItem = status.result ?: emptyList()
                refreshList(listItem)

                itemSaleDialog?.dismiss()
            }
        }
    }

    private fun refreshList(list: List<ItemSale>) {
        val hasData = list.isNotEmpty()
        binding.vgEmptyData.visibility = if (hasData) { View.GONE } else { View.VISIBLE }

        itemAdapter.submitList(list)
    }

    private fun handleError(errorMessage: String) {
        val msg = requireActivity().getString(R.string.dialog_error_msg, errorMessage)

        val btOk = {}
        dialogUtil.showErrorDialog(msg, btOk)
    }

    private fun setupListener() {
        binding.btNewItem.setOnClickListener { showDialogItemSale() }
    }

    private fun setupRecycler() {
        itemAdapter.setOnEditEvent { itemSale -> showDialogItemSale(itemSale) }
        itemAdapter.setOnDeleteEvent { itemSale -> showDialogDeleteItem(itemSale) }

        binding.recycler.adapter = itemAdapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun save(itemSale: ItemSale) {
        val idMessage = viewModel.validateItemSale(itemSale)

        if (idMessage >= 0) {
            itemSaleDialog?.showMessage(idMessage)
        } else {
            viewModel.handleEvent(ListItemEvent.SaveItem(itemSale))
        }
    }

    private fun showDialogItemSale(itemSale: ItemSale? = null) {
        itemSaleDialog = ItemSaleDialog (requireActivity(), saleId, itemSale)
        itemSaleDialog?.setOnSaveEvent { item -> save(item) }
        itemSaleDialog?.show()
    }

    private fun showDialogDeleteItem(itemSale: ItemSale) {
        val btOk = {
            viewModel.handleEvent(ListItemEvent.DeleteItem(itemSale))
        }

        dialogUtil.showDialog(
            getString(R.string.page_item_delete_title),
            getString(R.string.page_item_delete_msg, itemSale.product),
            getString(R.string.page_item_delete_bt_ok),
            btOk,
            getString(R.string.page_item_delete_bt_cancel),
            null)
    }

    private fun showDialogDeleteSale() {
        val btYes = {
            viewModel.handleEvent(ListItemEvent.DeleteSale)
        }

        dialogUtil.showDialog(
            getString(R.string.page_item_delete_sale_title),
            getString(R.string.page_item_delete_sale_msg),
            getString(R.string.page_item_delete_sale_bt_yes),
            btYes,
            getString(R.string.page_item_delete_sale_bt_no)
        ) { goBackToListSale() }
    }

    private fun goBackToListSale() {
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}
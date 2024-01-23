package cvb.com.br.vendaomie.presentation.page_list_sale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import cvb.com.br.vendaomie.R
import cvb.com.br.vendaomie.di.DefaultDispatcher
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.use_case.ItemSaleUseCase
import cvb.com.br.vendaomie.domain.use_case.SaleUseCase
import cvb.com.br.vendaomie.presentation.UIStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragListSaleViewModel @Inject constructor(
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val saleUseCase: SaleUseCase,
    private val itemSaleUseCase: ItemSaleUseCase
) : ViewModel() {

    private val mLoadData = MutableLiveData<UIStatus<List<Sale>>>()
    val loadData: LiveData<UIStatus<List<Sale>>>
        get() = mLoadData

    private val mDeleteData = MutableLiveData<UIStatus<List<Sale>>>()
    val deleteData: LiveData<UIStatus<List<Sale>>>
        get() = mDeleteData

    private val mInsertData = MutableLiveData<UIStatus<Long>>()
    val insertData: LiveData<UIStatus<Long>>
        get() = mInsertData

    private val mIsLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = mIsLoading.distinctUntilChanged()

    //--

    private var lastSaleIdInserted: Long = 0

    //--

    init {
        mIsLoading.addSource(mLoadData) {
            updateMediatorIsLoading()
        }
        mIsLoading.addSource(mDeleteData) {
            updateMediatorIsLoading()
        }
        mIsLoading.addSource(mInsertData) {
            updateMediatorIsLoading()
        }
    }

    private fun updateMediatorIsLoading() {
        mIsLoading.value = isLoading()
    }

    private fun isLoading(): Boolean {
        return mLoadData.value is UIStatus.Loading ||
               mDeleteData.value is UIStatus.Loading ||
               mInsertData.value is UIStatus.Loading
    }

    fun handleEvent(event: ListSaleEvent) {
        when (event) {
            is ListSaleEvent.LoadData -> { loadData() }

            is ListSaleEvent.CreateSaleRecord -> { createSale(event.client) }

            is ListSaleEvent.DeleteSaleRecord -> { deleteSale(event.sale) }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                mLoadData.value = UIStatus.Loading

                val listSale = getListSale()

                mLoadData.value = UIStatus.Success(listSale)
            } catch (error: Throwable) {
                mLoadData.value = UIStatus.Error(error)
            }
        }
    }

    private fun createSale(client: String) {
        viewModelScope.launch {
            try {
                mInsertData.value = UIStatus.Loading

                val sale = Sale(0, System.currentTimeMillis(), client)

                val saleId = saleUseCase.insertSale(sale)

                mInsertData.value = UIStatus.Success(saleId)
            } catch (error: Throwable) {
                mInsertData.value = UIStatus.Error(error)
            }
        }
    }

    private fun deleteSale(sale: Sale) {
        viewModelScope.launch {
            try {
                mDeleteData.value = UIStatus.Loading

                itemSaleUseCase.deleteAllItemsBySale(sale.id)

                saleUseCase.deleteSale(sale)

                val listSale = getListSale()

                mDeleteData.value = UIStatus.Success(listSale)
            } catch (error: Throwable) {
                mDeleteData.value = UIStatus.Error(error)
            }
        }
    }

    private suspend fun getListSale(): List<Sale> {
        val listSale = saleUseCase.getListSale()

        listSale.map { sale ->
            val listItemSale = itemSaleUseCase.getListItemsSaleBySale(sale.id)
            val total = itemSaleUseCase.getTotalBySale(sale.id)

            sale.listItemSale = listItemSale
            sale.total = total
        }

        return listSale
    }

    fun validateClient(client: String): Int {
        if (client.trim().isBlank()) {
            return R.string.page_sale_dialog_error_client
        }
        return -1
    }

    fun isValidNavigation(saleId: Long?): Boolean {
        saleId?.let {
            if (saleId > lastSaleIdInserted) {
                lastSaleIdInserted = saleId
                return true
            }
        }
        return false
    }
}
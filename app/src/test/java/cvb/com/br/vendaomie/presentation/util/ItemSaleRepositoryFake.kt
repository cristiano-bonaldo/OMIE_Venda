package cvb.com.br.vendaomie.presentation.util

import cvb.com.br.vendaomie.data.repository.util.SaleFactory
import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.repository.ItemSaleRepository
import cvb.com.br.vendaomie.domain.repository.SaleRepository

class ItemSaleRepositoryFake : ItemSaleRepository {

    private val list = mutableListOf<ItemSale>()

    private var isError: Boolean = false

    fun enableError() {
        isError = true
    }

    override suspend fun insert(itemSale: ItemSale) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        list.add(itemSale)
    }

    override suspend fun delete(itemSale: ItemSale) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        list.remove(itemSale)
    }

    override suspend fun update(itemSale: ItemSale) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        list.remove(itemSale)
        list.add(itemSale)
    }

    override suspend fun getListItemSaleBySale(saleId: Long): List<ItemSale> {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        return list.filter { item -> item.saleId == saleId }
    }

    override suspend fun deleteAllItemsBySale(saleId: Long) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        list.filter { item -> item.saleId == saleId }.forEach { item ->
            list.remove(item)
        }
    }
}
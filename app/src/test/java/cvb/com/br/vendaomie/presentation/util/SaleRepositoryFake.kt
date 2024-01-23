package cvb.com.br.vendaomie.presentation.util

import cvb.com.br.vendaomie.data.repository.util.SaleFactory
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.repository.SaleRepository

class SaleRepositoryFake : SaleRepository {

    private val list = mutableListOf<Sale>()

    private var isError: Boolean = false

    fun enableError() {
        isError = true
    }

    override suspend fun insert(sale: Sale): Long {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        val maxSale = list.maxByOrNull { itemSale -> itemSale.id }

        val newId = maxSale?.let { item -> item.id + 1 } ?: run { 1 }

        val newSale = sale.copy(id = newId)
        list.add(newSale)
        return newSale.id
    }

    override suspend fun delete(sale: Sale) {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        list.remove(sale)
    }

    override suspend fun getListSale(): List<Sale> {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        return list
    }

    override suspend fun getSaleById(saleId: Long): Sale {
        if (isError) {
            throw Throwable("ProductDataSource ERROR")
        }

        val sale = SaleFactory.createSale(saleId)

        val idx = list.indexOf(sale)

        return list[idx]
    }
}
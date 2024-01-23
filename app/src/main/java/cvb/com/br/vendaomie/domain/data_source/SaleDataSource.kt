package cvb.com.br.vendaomie.domain.data_source

import cvb.com.br.vendaomie.domain.model.Sale


interface SaleDataSource {

    suspend fun insert(sale: Sale): Long

    suspend fun delete(sale: Sale)

    suspend fun getListSale(): List<Sale>

    suspend fun getSaleById(saleId: Long): Sale
}
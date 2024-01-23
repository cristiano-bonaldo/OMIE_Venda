package cvb.com.br.vendaomie.domain.repository

import cvb.com.br.vendaomie.domain.model.Sale


interface SaleRepository {

    suspend fun insert(sale: Sale): Long

    suspend fun delete(sale: Sale)

    suspend fun getListSale(): List<Sale>

    suspend fun getSaleById(saleId: Long): Sale
}
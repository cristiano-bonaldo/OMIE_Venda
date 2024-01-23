package cvb.com.br.vendaomie.data.repository

import cvb.com.br.vendaomie.domain.data_source.SaleDataSource
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.repository.SaleRepository
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(private val saleDataSource: SaleDataSource) : SaleRepository {


    override suspend fun insert(sale: Sale): Long {
        return saleDataSource.insert(sale)
    }

    override suspend fun delete(sale: Sale) {
        saleDataSource.delete(sale)
    }

    override suspend fun getListSale(): List<Sale> {
        return saleDataSource.getListSale()
    }

    override suspend fun getSaleById(saleId: Long): Sale {
        return saleDataSource.getSaleById(saleId)
    }
}
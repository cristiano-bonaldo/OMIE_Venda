package cvb.com.br.vendaomie.data.data_source.local

import cvb.com.br.vendaomie.data.db.dao.SaleDao
import cvb.com.br.vendaomie.data.mapper.toSale
import cvb.com.br.vendaomie.data.mapper.toSaleEntity
import cvb.com.br.vendaomie.domain.data_source.SaleDataSource
import cvb.com.br.vendaomie.domain.model.Sale
import javax.inject.Inject

class LocalSaleDataSource @Inject constructor(private val saleDao: SaleDao) : SaleDataSource {

    override suspend fun insert(sale: Sale): Long {
        val saleEntity = sale.toSaleEntity()
        return saleDao.insert(saleEntity)
    }

    override suspend fun delete(sale: Sale) {
        val saleEntity = sale.toSaleEntity()
        saleDao.delete(saleEntity)
    }

    override suspend fun getListSale(): List<Sale> {
        val listSaleEntity = saleDao.getListSale()
        return listSaleEntity.map { saleEntity -> saleEntity.toSale() }
    }

    override suspend fun getSaleById(saleId: Long): Sale {
        val saleEntity = saleDao.getSaleById(saleId)
        return saleEntity.toSale()
    }

}
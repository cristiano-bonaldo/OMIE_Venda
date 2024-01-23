package cvb.com.br.vendaomie.data.data_source.local

import cvb.com.br.vendaomie.data.db.dao.ItemSaleDao
import cvb.com.br.vendaomie.data.mapper.toItemSale
import cvb.com.br.vendaomie.data.mapper.toItemSaleEntity
import cvb.com.br.vendaomie.domain.data_source.ItemSaleDataSource
import cvb.com.br.vendaomie.domain.model.ItemSale
import javax.inject.Inject

class LocalItemSaleDataSource @Inject constructor(private val itemSaleDao: ItemSaleDao) :
    ItemSaleDataSource {

    override suspend fun insert(itemSale: ItemSale) {
        val itemSaleEntity = itemSale.toItemSaleEntity()
        itemSaleDao.insert(itemSaleEntity)
    }

    override suspend fun delete(itemSale: ItemSale) {
        val itemSaleEntity = itemSale.toItemSaleEntity()
        itemSaleDao.delete(itemSaleEntity)
    }

    override suspend fun update(itemSale: ItemSale) {
        val itemSaleEntity = itemSale.toItemSaleEntity()
        itemSaleDao.update(itemSaleEntity)
    }

    override suspend fun getListItemSaleBySale(saleId: Long): List<ItemSale> {
        val listItemSaleEntity = itemSaleDao.getListItemSaleBySale(saleId)
        return listItemSaleEntity.map { itemSaleEntity -> itemSaleEntity.toItemSale() }
    }

    override suspend fun deleteAllItemsBySale(saleId: Long) {
        itemSaleDao.deleteAllItemsBySale(saleId)
    }
}
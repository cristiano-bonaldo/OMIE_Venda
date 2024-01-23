package cvb.com.br.vendaomie.data.repository

import cvb.com.br.vendaomie.domain.data_source.ItemSaleDataSource
import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.domain.repository.ItemSaleRepository
import javax.inject.Inject

class ItemSaleRepositoryImpl @Inject constructor(private val itemSaleDataSource: ItemSaleDataSource) :
    ItemSaleRepository {

    override suspend fun insert(itemSale: ItemSale) {
        itemSaleDataSource.insert(itemSale)
    }

    override suspend fun delete(itemSale: ItemSale) {
        itemSaleDataSource.delete(itemSale)
    }

    override suspend fun update(itemSale: ItemSale) {
        itemSaleDataSource.update(itemSale)
    }

    override suspend fun getListItemSaleBySale(saleId: Long): List<ItemSale> {
        return itemSaleDataSource.getListItemSaleBySale(saleId)
    }

    override suspend fun deleteAllItemsBySale(saleId: Long) {
        itemSaleDataSource.deleteAllItemsBySale(saleId)
    }
}
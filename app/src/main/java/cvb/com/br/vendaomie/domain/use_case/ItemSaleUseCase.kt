package cvb.com.br.vendaomie.domain.use_case

import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.domain.repository.ItemSaleRepository
import javax.inject.Inject

class ItemSaleUseCase @Inject constructor(private val itemSaleRepository: ItemSaleRepository) {

    suspend fun getListItemsSaleBySale(saleId: Long): List<ItemSale> {
        return itemSaleRepository.getListItemSaleBySale(saleId)
    }

    suspend fun getTotalBySale(saleId: Long): Double {
        val total =  itemSaleRepository.getListItemSaleBySale(saleId).sumOf { itemSale ->
            itemSale.unitValue * itemSale.quantity
        }
        return total
    }

    suspend fun insertItem(itemSale: ItemSale) {
        itemSaleRepository.insert(itemSale)
    }

    suspend fun updateItem(itemSale: ItemSale) {
        itemSaleRepository.update(itemSale)
    }

    suspend fun deleteItem(itemSale: ItemSale) {
        itemSaleRepository.delete(itemSale)
    }

    suspend fun deleteAllItemsBySale(saleId: Long) {
        itemSaleRepository.deleteAllItemsBySale(saleId)
    }
}


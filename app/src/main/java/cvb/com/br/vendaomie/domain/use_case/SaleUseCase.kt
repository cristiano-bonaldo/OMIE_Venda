package cvb.com.br.vendaomie.domain.use_case

import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.repository.SaleRepository
import cvb.com.br.vendaomie.presentation.page_list_sale.adapter.ListItem
import cvb.com.br.vendaomie.util.DateTimeUtil.convertTimeMillisToDate
import javax.inject.Inject

class SaleUseCase @Inject constructor(private val saleRepository: SaleRepository) {

    suspend fun getListSale(): List<Sale> {
        return saleRepository.getListSale()
    }

    suspend fun insertSale(sale: Sale): Long {
        return saleRepository.insert(sale)
    }

    suspend fun deleteSale(sale: Sale) {
        return saleRepository.delete(sale)
    }

    suspend fun getSaleById(saleId: Long): Sale {
        return saleRepository.getSaleById(saleId)
    }

    suspend fun getListItem(): List<ListItem> {
        val saleList = saleRepository.getListSale()

        val dateList = saleList.distinctBy { sale -> convertTimeMillisToDate(sale.date) }

        val result = mutableListOf<ListItem>()

        dateList.forEach { dateSale ->
            result.add(ListItem.HeadItem(convertTimeMillisToDate(dateSale.date)))
            val list = saleList.filter { sale -> convertTimeMillisToDate(sale.date) == convertTimeMillisToDate(dateSale.date) }
            list.forEach { sale ->
                result.add(ListItem.InfoItem(sale))
            }
        }

        return result
    }
}


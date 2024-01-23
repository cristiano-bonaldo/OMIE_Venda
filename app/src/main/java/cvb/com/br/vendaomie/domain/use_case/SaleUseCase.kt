package cvb.com.br.vendaomie.domain.use_case

import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.repository.SaleRepository
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
}


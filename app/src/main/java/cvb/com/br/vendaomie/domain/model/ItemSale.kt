package cvb.com.br.vendaomie.domain.model

data class ItemSale(
    val id: Long,
    val saleId: Long,
    val product: String,
    val unitValue: Double,
    val quantity: Double
)

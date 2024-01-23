package cvb.com.br.vendaomie.util

import android.content.Context
import cvb.com.br.vendaomie.R
import cvb.com.br.vendaomie.domain.model.Sale

object SaleUtil {

    fun getInfoPurchaseFormatted(context: Context, sale: Sale): String {
        /*
        Exemplo:

        Cliente: Teste
        Nº de Transação: 10050
        Data: 21/10/2023 15:05
        Valor Total R$: 125.00
        Itens
        ------------------
        */

        val stringBuilder = StringBuilder()

        val infoPurchase = context.getString(R.string.share_data_info_sale,
            sale.client,
            sale.id.toString(),
            DateTimeUtil.convertTimeMillisToString(sale.date),
            StringUtil.formatValue(sale.total))

        stringBuilder.append(infoPurchase)

        /*
        Exemplo:
        -----------
        Item: 1
        Produto: Coleira
        Quantidade: 2
        Valor Unitário 30.00
        Valor R$: 60.00
        */

        sale.listItemSale.forEachIndexed { idx, itemPurchase ->
            val item = idx + 1
            val prodTotal = itemPurchase.unitValue * itemPurchase.quantity

            val infoItem = context.getString(R.string.share_data_info_item_sale,
                item.toString(),
                itemPurchase.product,
                itemPurchase.quantity.toString(),
                StringUtil.formatValue(itemPurchase.unitValue),
                StringUtil.formatValue(prodTotal)
            )

            stringBuilder.append(infoItem)
        }

        return stringBuilder.toString()
    }
}
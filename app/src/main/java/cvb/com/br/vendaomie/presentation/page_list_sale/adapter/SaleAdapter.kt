package cvb.com.br.vendaomie.presentation.page_list_sale.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cvb.com.br.vendaomie.databinding.ItemListSaleBinding
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.util.DateTimeUtil
import cvb.com.br.vendaomie.util.StringUtil
import javax.inject.Inject

class SaleAdapter @Inject constructor() : RecyclerView.Adapter<SaleAdapter.SaleViewHolder>() {

    private var onShareEvent: ((sale: Sale) -> Unit)? = null
    private var onEditEvent: ((sale: Sale) -> Unit)? = null
    private var onDeleteEvent: ((sale: Sale) -> Unit)? = null

    private var listSale: List<Sale> = listOf()

    fun submitList(listSale: List<Sale>) {
        this.listSale = listSale
        notifyDataSetChanged()
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val binding = ItemListSaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SaleViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = listSale[position]
        holder.bind(sale)
    }

    override fun getItemCount(): Int {
        return listSale.size
    }

    fun setOnDeleteEvent(deleteEvent: (sale: Sale) -> Unit) {
        this.onDeleteEvent = deleteEvent
    }

    fun setOnEditEvent(editEvent: (sale: Sale) -> Unit) {
        this.onEditEvent = editEvent
    }

    fun setOnShareEvent(shareEvent: (sale: Sale) -> Unit) {
        this.onShareEvent = shareEvent
    }

    //--------------------------

    inner class SaleViewHolder(private val binding: ItemListSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sale: Sale) {
            binding.apply {
                this.ivShare.setOnClickListener {
                    onShareEvent?.invoke(sale)
                }

                this.ivDelete.setOnClickListener {
                    onDeleteEvent?.invoke(sale)
                }

                this.ivEdit.setOnClickListener {
                    onEditEvent?.invoke(sale)
                }

                this.tvClient.text = sale.client
                this.tvId.text = sale.id.toString()
                this.tvDate.text = DateTimeUtil.convertTimeMillisToString(sale.date)
                this.tvTotal.text = StringUtil.formatValue(sale.total)
            }
        }
    }
}
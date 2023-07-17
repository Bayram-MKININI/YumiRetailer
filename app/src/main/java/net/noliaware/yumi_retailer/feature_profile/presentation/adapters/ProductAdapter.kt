package net.noliaware.yumi_retailer.feature_profile.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.SHORT_DATE_FORMAT
import net.noliaware.yumi_retailer.commun.presentation.adapters.ItemViewHolder
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.parseDateToFormat
import net.noliaware.yumi_retailer.feature_profile.domain.model.Product
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductListItemView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductListItemView.*

class ProductAdapter : PagingDataAdapter<Product, ItemViewHolder<ProductListItemView>>(
    ProductComparator
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ItemViewHolder<ProductListItemView>(
        parent.inflate(R.layout.product_item_layout)
    )

    override fun onBindViewHolder(holder: ItemViewHolder<ProductListItemView>, position: Int) {
        getItem(position)?.let { alert ->
            holder.heldItemView.fillViewWithData(
                mapAdapter(alert, holder)
            )
        }
    }

    private fun mapAdapter(
        product: Product,
        holder: ItemViewHolder<ProductListItemView>
    ) = ProductItemViewAdapter(
        label = product.productLabel,
        startDate = product.productStartDate?.parseDateToFormat(SHORT_DATE_FORMAT),
        expiryDate = product.productExpiryDate?.parseDateToFormat(SHORT_DATE_FORMAT),
        price = holder.heldItemView.context.getString(
            R.string.price_format,
            product.productPrice.formatNumber()
        ),
        planned = product.expectedVoucherCount.formatNumber(),
        used = holder.heldItemView.context.getString(
            R.string.product_value_format,
            product.usedVoucherCount.formatNumber(),
            product.usedVoucherAmount.formatNumber()
        ),
        cancelled = holder.heldItemView.context.getString(
            R.string.product_value_format,
            product.cancelledVoucherCount.formatNumber(),
            product.cancelledVoucherAmount.formatNumber()
        )
    )

    object ProductComparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
}
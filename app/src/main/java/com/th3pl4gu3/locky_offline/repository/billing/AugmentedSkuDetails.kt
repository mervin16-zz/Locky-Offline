package com.th3pl4gu3.locky_offline.repository.billing

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.billingclient.api.SkuDetails

@Entity(tableName = "augmented_skuDetails")
data class AugmentedSkuDetails(
    @PrimaryKey val sku: String,
    var purchased: Boolean, /* Not in SkuDetails; it's the augmentation */
    val type: String?,
    val price: String?,
    val title: String?,
    val description: String?,
    val originalJson: String?
) {

    companion object {
        fun translate(purchased: Boolean, skuDetails: SkuDetails) = AugmentedSkuDetails(
            skuDetails.sku,
            purchased,
            skuDetails.type,
            skuDetails.price,
            skuDetails.title,
            skuDetails.description,
            skuDetails.originalJson
        )
    }

    internal fun getSkuDetails() = SkuDetails(this.originalJson)
}
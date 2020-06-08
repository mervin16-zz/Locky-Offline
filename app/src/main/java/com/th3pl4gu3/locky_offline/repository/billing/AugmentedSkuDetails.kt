package com.th3pl4gu3.locky_offline.repository.billing

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.billingclient.api.SkuDetails

@Entity(tableName = "augmented_skuDetails")
data class AugmentedSkuDetails(
    val isAvailable: Boolean, /* Not in SkuDetails; it's the augmentation */
    @PrimaryKey val sku: String,
    val type: String?,
    val price: String?,
    val title: String?,
    val description: String?,
    val originalJson: String?
) {

    companion object {
        fun translate(isAvailable: Boolean, skuDetails: SkuDetails) = AugmentedSkuDetails(
            isAvailable,
            skuDetails.sku,
            skuDetails.type,
            skuDetails.price,
            skuDetails.title,
            skuDetails.description,
            skuDetails.originalJson
        )
    }

}
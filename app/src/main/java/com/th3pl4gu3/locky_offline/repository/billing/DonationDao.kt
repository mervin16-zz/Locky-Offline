package com.th3pl4gu3.locky_offline.repository.billing

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/*
* Donation CRUD
*/
@Dao
interface DonationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(augmentedSkuDetails: AugmentedSkuDetails)

    @Query("SELECT * FROM augmented_skuDetails WHERE sku = :sku")
    suspend fun getAugmentedSkuDetails(sku: String): AugmentedSkuDetails?

}

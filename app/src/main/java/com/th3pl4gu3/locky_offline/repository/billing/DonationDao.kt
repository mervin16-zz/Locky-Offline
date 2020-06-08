package com.th3pl4gu3.locky_offline.repository.billing

import androidx.room.*
import com.th3pl4gu3.locky_offline.core.main.*

@Dao
interface DonationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(augmentedSkuDetails: AugmentedSkuDetails)

    @Query("SELECT * FROM augmented_skuDetails WHERE sku = :sku")
    suspend fun getAugmentedSkuDetails(sku: String): AugmentedSkuDetails?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cookie: Cookie)

    @Query("SELECT * FROM donation_cookie LIMIT 1")
    fun getCookie(): Cookie?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sandwich: Sandwich)

    @Query("SELECT * FROM donation_sandwich LIMIT 1")
    fun getSandwich(): Sandwich?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(milkshake: Milkshake)

    @Query("SELECT * FROM donation_milkshake LIMIT 1")
    fun getMilkshake(): Milkshake?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(burger: Burger)

    @Query("SELECT * FROM donation_burger LIMIT 1")
    fun getBurger(): Burger?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gift: Gift)

    @Query("SELECT * FROM donation_gift LIMIT 1")
    fun getGift(): Gift?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(star: Star)

    @Query("SELECT * FROM donation_star LIMIT 1")
    fun getStar(): Star?

    @Transaction
    fun exists(donation: Donation): Boolean = when (donation) {
        is Cookie -> with(getCookie()) {
            this != null && this.hasPurchased
        }

        is Sandwich -> with(getSandwich()) {
            this != null && this.hasPurchased
        }
        is Milkshake -> with(getMilkshake()) {
            this != null && this.hasPurchased
        }
        is Burger -> with(getBurger()) {
            this != null && this.hasPurchased
        }
        is Gift -> with(getGift()) {
            this != null && this.hasPurchased
        }
        is Star -> with(getStar()) {
            this != null && this.hasPurchased
        }
        else -> false
    }
}

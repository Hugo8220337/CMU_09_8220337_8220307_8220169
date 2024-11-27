package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Quote

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quote WHERE date = :date")
    suspend fun getQuoteByDate(date: String): Quote

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: Quote): Long


}
package com.lincoln4791.dailyexpensemanager.roomDB

import androidx.room.*
import com.lincoln4791.dailyexpensemanager.model.MC_Cards
import com.lincoln4791.dailyexpensemanager.model.MC_MonthlyReport
import com.lincoln4791.dailyexpensemanager.model.MC_Posts
import com.lincoln4791.dailyexpensemanager.modelClass.Banner

@Dao
interface DatabaseDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg mcPosts: MC_Posts)

    @Query("SELECT * FROM MC_Posts")
    suspend fun loadAllTransactions(): MutableList<MC_Posts>


    @Query("SELECT * FROM MC_Posts where postType = :type ")
    suspend fun loadTypeWise(type: String): List<MC_Posts>


    @Query("SELECT * FROM MC_Posts where postYear = :year ")
    suspend fun loadYearWise(year: String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postType = :type ")
    suspend fun loadYearTypeWise(year: String,type:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postCategory = :category ")
    suspend fun loadYearCategoryWise(year: String,category:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postType = :type and postCategory = :category ")
    suspend fun loadYearTypeCategoryWise(year: String,type : String,category:String): List<MC_Posts>


    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month ")
    suspend fun loadYearMonthWise(year: String,month:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postType=:type ")
    suspend  fun loadYearMonthTypeWise(year: String,month:String,type:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postCategory=:category ")
    suspend fun loadYearMonthCategoryWise(year: String,month:String,category:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postType =:type and  postCategory=:category ")
    suspend fun loadYearMonthTypeCategoryWise(year: String,month:String,type:String,category:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postDay=:day ")
    suspend fun loadYearMonthDayWise(year: String,month:String,day:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postDay =:day and  postType=:type ")
    suspend fun loadYearMonthDayTypeWise(year: String,month:String,day:String,type:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postDay =:day and  postType=:category ")
    suspend fun loadYearMonthDayCategoryWise(year: String,month:String,day:String,category:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postDay =:day and  postType=:type and postCategory=:category ")
    suspend fun loadYearMonthDayTypeCategoryWise(year: String,month:String,day:String,type:String,category:String): List<MC_Posts>

    @Query("delete from MC_Posts where id=:id")
    suspend fun delete(id:String)

   // @Query("Delete from mc_posts")
    @Query("Delete from MC_Posts")
    suspend fun deleteAll()


    //Group BY

    @Query("SELECT postCategory,SUM(postAmount) as postAmount, (cast(SUM(postAmount) as double) /(SELECT SUM(postAmount) FROM MC_Posts WHERE postType = :type))*100 as amountPercent FROM MC_Posts where postYear = :year and postMonth = :month and postType = :type and postCategory =:category GROUP BY postCategory")
    suspend fun loadYearMonthTypeCategoryWiseByGroup(year: String, month:String, type : String,category:String): List<MC_MonthlyReport>

    @Query("SELECT postCategory,SUM(postAmount) as postAmount, (cast(SUM(postAmount) as double) /(SELECT SUM(postAmount) FROM MC_Posts WHERE postType = :type AND postYear=:year AND postMonth=:month))*100 as amountPercent FROM MC_Posts where postYear = :year and postMonth = :month and postType = :type GROUP BY postCategory")
    suspend fun loadYearMonthTypeWiseByGroup(year: String, month:String, type : String): List<MC_MonthlyReport>

    @Query("SELECT postCategory,SUM(postAmount) as postAmount, (cast(SUM(postAmount) as double) /(SELECT SUM(postAmount) FROM MC_Posts WHERE postType = :type))*100 as amountPercent FROM MC_Posts where postYear = :year and postType = :type GROUP BY postCategory")
    suspend fun loadYearTypeWiseByGroup(year: String, type : String): List<MC_MonthlyReport>

    @Query("SELECT SUM(postAmount) FROM  MC_Posts WHERE postYear = :year and postMonth = :month and  postType=:type")
    suspend fun loadYearMonthTypeTotal(year: String, month:String, type : String): Int

    @Query("SELECT SUM(postAmount) FROM  MC_Posts WHERE postYear = :year and  postType=:type ")
    suspend fun loadYearTypeTotal(year: String, type : String): Int


    @Query("SELECT SUM(postAmount) FROM  MC_Posts WHERE postYear = :year and postMonth = :month and postType=:typeExpense - (SELECT SUM(postAmount) FROM  MC_Posts WHERE postYear = :year and postMonth = :month and postType=:typeIncome)")
    suspend fun loadYearMonthBalance(year: String, month:String,typeIncome : String,typeExpense : String): Int

    @Query("SELECT SUM(postAmount) FROM  MC_Posts WHERE postYear = :year and postType=:typeExpense - (SELECT SUM(postAmount) FROM  MC_Posts WHERE postYear = :year and postType=:typeIncome)")
    suspend fun loadYearBalance(year: String,typeIncome : String,typeExpense : String): Int





    //Cards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(vararg card: MC_Cards)


    @Query("SELECT * FROM MC_Cards WHERE cardType = :cardType ORDER BY id DESC")
    suspend fun loadAllExpenseCards(cardType:String): MutableList<MC_Cards>

    @Query("SELECT * FROM MC_Cards WHERE cardType = :cardType ORDER BY id DESC")
    suspend fun loadAllIncomeCards(cardType:String): MutableList<MC_Cards>

    @Query("SELECT postCategory FROM MC_Posts WHERE postType = :cardType GROUP BY postCategory ORDER BY id DESC")
    suspend fun getAllCardsByTypeAsArrayString(cardType:String): Array<String>


    @Query("Delete from mc_cards where cardName =:cardName AND cardType = :cardType ")
    suspend fun deleteExpenseCardByName(cardName : String,cardType: String)

    @Query("Delete from mc_cards where cardName =:cardName AND cardType =:cardType")
    suspend fun deleteIncomeCardByName(cardName : String,cardType: String)



    //Banners
    @Query("SELECT * FROM banner")
    suspend fun getAllBanner() : MutableList<Banner>

    @Query("SELECT * FROM banner WHERE is_active=:value")
    suspend fun getAllActiveBanner(value:String) : MutableList<Banner>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(vararg banner: Banner)

    @Delete
    suspend fun deleteBanner(vararg banner: Banner)

    @Query("DELETE FROM banner")
    suspend fun deleteAllBanners()

}
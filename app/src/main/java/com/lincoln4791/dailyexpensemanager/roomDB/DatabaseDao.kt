package com.lincoln4791.dailyexpensemanager.roomDB

import androidx.room.*
import com.lincoln4791.dailyexpensemanager.model.MC_Posts

@Dao
interface DatabaseDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg mcPosts: MC_Posts)

    @Query("SELECT * FROM MC_Posts")
    fun loadAllTransactions(): MutableList<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postType = :type ")
    fun loadTypeWise(type: String): List<MC_Posts>


    @Query("SELECT * FROM MC_Posts where postYear = :year ")
    fun loadYearWise(year: String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postType = :type ")
    fun loadYearTypeWise(year: String,type:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postCategory = :category ")
    fun loadYearCategoryWise(year: String,category:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postType = :type and postCategory = :category ")
    fun loadYearTypeCategoryWise(year: String,type : String,category:String): List<MC_Posts>


    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month ")
    fun loadYearMonthWise(year: String,month:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postType=:type ")
    fun loadYearMonthTypeWise(year: String,month:String,type:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postCategory=:category ")
    fun loadYearMonthCategoryWise(year: String,month:String,category:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postType =:type and  postCategory=:category ")
    fun loadYearMonthTypeCategoryWise(year: String,month:String,type:String,category:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postDay=:day ")
    fun loadYearMonthDayWise(year: String,month:String,day:String): List<MC_Posts>

    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postDay =:day and  postType=:type ")
    fun loadYearMonthDayTypeWise(year: String,month:String,day:String,type:String): List<MC_Posts>


    @Query("SELECT * FROM MC_Posts where postYear = :year and postMonth = :month and postDay =:day and  postType=:type and postCategory=:category ")
    fun loadYearMonthDayTypeCategoryWise(year: String,month:String,day:String,type:String,category:String): List<MC_Posts>

    @Query("delete from MC_Posts where id=:id")
    fun delete(id:String)

    @Query("Delete from mc_posts")
    fun deleteAll()







/*    @Query("delete FROM mytable where id= :id" )
    fun delete(id:Int)*/

/*    @Query("SELECT * FROM MyTable WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("delete  FROM user")
    fun deleteAll()

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    suspend fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)


    @Query("SELECT * FROM user WHERE uid = :id")
    fun loadByID( id : Int): User


    @Query("SELECT * FROM user WHERE uid in (:ids)")
    fun loadByIDs( vararg ids:Int): MutableList<User>

    @Query("SELECT * FROM user WHERE uid between :idFrom AND :idTo ")
    fun loadByIDs( idFrom:Int, idTo : Int): MutableList<User>


    @Query("Delete  FROM user WHERE uid = :id")
    fun deleteByID( id : Int)

    @Query("Select distinct first_name FROM user")
    fun loadDistinctName() : MutableList<String>

    @Query("update user set first_name = :firstName, last_name = :lastName where uid = :id")
    fun updateNameByID(firstName : String, lastName : String, id:Int)*/

}
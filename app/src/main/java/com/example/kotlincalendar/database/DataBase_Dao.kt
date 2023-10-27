package com.example.kotlincalendar.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate

@Dao
interface User_Dao {

    @Query("SELECT * FROM User")
    fun Select_frd(): List<User>

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM frdlist_db WHERE (user1=:userEmail AND User2=:friendEmail) OR (User1=:friendEmail AND User2=:userEmail)")
    fun areFriends(userEmail: String, friendEmail: String): frdlist_db?

    @Query("SELECT *FROM frdadd_db WHERE (Sender_ID=:userEmail AND Receiver_ID=:friendEmail)")
    fun isRequestPending(userEmail:String, friendEmail:String): frdadd_db?

    @Query("SELECT * FROM User WHERE Email = :email AND Password = :password")
    fun get_Login(email: String, password: String): User?

    @Query("SELECT * FROM User WHERE Email=:email")
    fun getUserByEmail(email:String): User?
}
@Dao
interface frdlist_db_Dao{
    @Insert
    fun insertFriend(friend: frdlist_db)

    @Delete
    fun deleteFriend(friend: frdlist_db)

    @Query("SELECT * FROM frdlist_db WHERE user1 = :userId OR user2 = :userId")
    fun getFriends(userId: String): List<frdlist_db>
}

@Dao
interface frdadd_db_Dao{
    @Insert
    fun insertRequest(frdaddDB: frdadd_db)

    @Delete
    fun deleteRequest(frdaddDB: frdadd_db)

    @Query("SELECT * FROM frdadd_db WHERE Receiver_ID = :userId")
    fun getRequests(userId: String): List<frdadd_db>

    @Query("SELECT * FROM frdadd_db WHERE Sender_ID = :userId")
    fun getSentRequests(userId: String): List<frdadd_db>
}

@Dao
interface UserCalendar_Dao{
    @Insert
    fun insertSchedule(Scehdule: UserCalendar)
    @Delete
    fun deleteSchedule(Scehdule: UserCalendar)
    @Update
    fun updateSchedule(Scehdule: UserCalendar)

    @Query("SELECT * FROM UserCalendar WHERE Calendar_UserEmail= :userEmail_management AND Schedule_LocalDate=:LocalDate_management")
    fun seleteSchedule(userEmail_management:String, LocalDate_management:LocalDate):List<UserCalendar>


}

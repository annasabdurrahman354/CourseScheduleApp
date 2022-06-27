package com.dicoding.courseschedule.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface CourseDao {
    @Transaction
    @RawQuery(observedEntities = [Course::class])
    fun getNearestSchedule(query: SupportSQLiteQuery): LiveData<Course?>

    @Transaction
    @RawQuery(observedEntities = [Course::class])
    fun getAll(query: SupportSQLiteQuery): DataSource.Factory<Int, Course>

    @Transaction
    @Query("SELECT * from course WHERE id == :id LIMIT 1")
    fun getCourse(id: Int): LiveData<Course>

    @Transaction
    @RawQuery(observedEntities = [Course::class])
    fun getTodaySchedule(query: SupportSQLiteQuery): List<Course>

    @Insert
    fun insert(course: Course)

    @Delete
    fun delete(course: Course)

    //@Transaction
    //@RawQuery(observedEntities = [Course::class])
    //fun sort(params: String): DataSource.Factory<Int, Course>
}
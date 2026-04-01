package com.example.smsledger.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ParsingRuleDao {
    @Query("SELECT * FROM parsing_rules")
    fun getParsingRules(): Flow<List<ParsingRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: ParsingRuleEntity)

    @Update
    suspend fun update(rule: ParsingRuleEntity)

    @Delete
    suspend fun delete(rule: ParsingRuleEntity)
}

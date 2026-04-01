package com.example.smsledger.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smsledger.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

fun CategoryEntity.toDomain() = Category(id = id, name = name)
fun Category.toEntity() = CategoryEntity(id = id, name = name)

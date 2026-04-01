package com.example.smsledger.data.repository

import com.example.smsledger.data.local.CategoryDao
import com.example.smsledger.data.local.toDomain
import com.example.smsledger.data.local.toEntity
import com.example.smsledger.domain.model.Category
import com.example.smsledger.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {
    override fun getCategories(): Flow<List<Category>> = 
        dao.getCategories().map { list -> list.map { it.toDomain() } }

    override suspend fun addCategory(category: Category) = dao.insert(category.toEntity())
    override suspend fun updateCategory(category: Category) = dao.update(category.toEntity())
    override suspend fun deleteCategory(category: Category) = dao.delete(category.toEntity())
}

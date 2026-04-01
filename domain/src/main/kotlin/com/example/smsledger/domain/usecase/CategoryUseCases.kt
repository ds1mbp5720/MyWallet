package com.example.smsledger.domain.usecase

import com.example.smsledger.domain.model.Category
import com.example.smsledger.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(private val repository: CategoryRepository) {
    operator fun invoke(): Flow<List<Category>> = repository.getCategories()
}

class AddCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category) = repository.addCategory(category)
}

class UpdateCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category) = repository.updateCategory(category)
}

class DeleteCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category) = repository.deleteCategory(category)
}

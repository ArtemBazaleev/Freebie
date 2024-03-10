package com.freebie.frieebiemobile.ui.company.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.ui.category.domain.usecase.GetCategoryRepositoryUseCase
import com.freebie.frieebiemobile.ui.category.mapper.CategoryMapper
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyCreationParams
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalCompanyLink
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalLinkType
import com.freebie.frieebiemobile.ui.company.domain.model.Locale
import com.freebie.frieebiemobile.ui.company.domain.usecase.CreateCompanyUseCase
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyCreationUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class CreateCompanyViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoryRepositoryUseCase,
    private val createCompany: CreateCompanyUseCase,
    private val mapper: CategoryMapper
) : ViewModel() {

    private val _state = MutableStateFlow(CompanyCreationUiModel(emptyList(), emptyList()))

    private val locales = ConcurrentHashMap<String, Locale>()
    private val links = ConcurrentHashMap<ExternalLinkType, ExternalCompanyLink>()
    private var currentCategoryId: String? = null
    private var city: String? = null

    val state: Flow<CompanyCreationUiModel>
        get() = _state

    init {
        requestCategories()
    }

    private fun requestCategories() {
        viewModelScope.launch(Dispatchers.Default) {
            getCategoriesUseCase
                .invoke()
                .onSuccess { categories ->
                    _state.emit(
                        CompanyCreationUiModel(
                            categories = categories.map(mapper::mapToUi),
                            cities = listOf("Limassol", "Nikosia", "Pafos", "Lefkosia", "Ayia Napa")
                        )
                    )
                }
                .onFailure {
                    Log.d("CreateCompanyViewModel", it.message ?: "")
                }
        }
    }

    fun addLocale(langCode: String, description: String, name: String) {
        locales[langCode] = Locale(langCode, description, name)
    }

    fun setCategory(name: String) {
        currentCategoryId = _state.value.categories.findLast { it.name == name }?.id
    }

    fun setCity(name: String) {
        city = name
    }

    fun addLink(link: ExternalCompanyLink) {
        links[link.type] = link
    }

    fun createCompany() {
        if (areFieldsFilled()) {
            Log.d("CreateCompanyViewModel",  "start company creation")
            viewModelScope.launch {
                createCompany.createCompany(
                    CompanyCreationParams(
                        categoryId = currentCategoryId!!,
                        avatar = null,
                        locale = locales.values.toList(),
                        links = links.values.toList(),
                        city = city!!
                    )
                ).onSuccess {
                    Log.d("CreateCompanyViewModel",  "Company created !!")
                }.onFailure {
                    it.printStackTrace()
                    Log.e("CreateCompanyViewModel", it.message ?: "")
                }
            }
        } else {
            Log.d("CreateCompanyViewModel",  "areFieldsFilled = false")
        }
    }

    private fun areFieldsFilled(): Boolean {
        val isLocalesFilled = locales.values.none { !it.isFilled() }
        return isLocalesFilled && currentCategoryId != null
    }
}
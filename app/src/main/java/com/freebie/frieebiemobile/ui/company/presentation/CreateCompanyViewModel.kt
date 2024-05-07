package com.freebie.frieebiemobile.ui.company.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.ui.category.domain.model.CategoryModel
import com.freebie.frieebiemobile.ui.category.domain.usecase.GetCategoryRepositoryUseCase
import com.freebie.frieebiemobile.ui.category.mapper.CategoryMapper
import com.freebie.frieebiemobile.ui.city.domain.model.CityModel
import com.freebie.frieebiemobile.ui.city.domain.usecase.GetCityRepositoryUseCase
import com.freebie.frieebiemobile.ui.city.mapper.CityMapper
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyCreationParams
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalCompanyLink
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalLinkType
import com.freebie.frieebiemobile.ui.company.domain.model.Locale
import com.freebie.frieebiemobile.ui.company.domain.usecase.CreateCompanyUseCase
import com.freebie.frieebiemobile.ui.company.domain.usecase.GetEditCompanyInfoUseCase
import com.freebie.frieebiemobile.ui.company.domain.usecase.UpdateCompanyUseCase
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyCreationEvent
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyCreationUiModel
import com.freebie.frieebiemobile.ui.company.presentation.model.CreateCompanyFieldError
import com.freebie.frieebiemobile.upload.UploadRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class CreateCompanyViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoryRepositoryUseCase,
    private val createCompany: CreateCompanyUseCase,
    private val updateCompany: UpdateCompanyUseCase,
    private val getEditCompanyInfo: GetEditCompanyInfoUseCase,
    private val mapper: CategoryMapper,
    private val uploadRepositoryImpl: UploadRepositoryImpl,
    private val getCitiesUseCase: GetCityRepositoryUseCase,
    private val cityMapper: CityMapper
) : ViewModel() {

    private val _state = MutableStateFlow(CompanyCreationUiModel(emptyList(), emptyList()))
    private val _events = Channel<CompanyCreationEvent>(Channel.BUFFERED)

    private val locales = ConcurrentHashMap<String, Locale>()
    private val links = ConcurrentHashMap<ExternalLinkType, ExternalCompanyLink>()
    private var currentCategoryId: String? = null
    private var currentCityId: String? = null

    // if companyId then update
    @Volatile
    private var companyId: String? = null

    val state: Flow<CompanyCreationUiModel>
        get() = _state

    val events: Flow<CompanyCreationEvent>
        get() = _events.receiveAsFlow()

    private suspend fun handleCategoriesAndCities(categories: List<CategoryModel>, cities: List<CityModel>) {
        _state.emit(
            CompanyCreationUiModel(
                categories = categories.map(mapper::mapToUi),
                cities = cities.map (cityMapper::mapToUi)
            )
        )
    }

    fun addLocale(langCode: String, description: String, name: String) {
        locales[langCode] = Locale(langCode, description, name)
        areFieldsFilled()
    }

    fun setCategory(name: String) {
        areFieldsFilled()
        currentCategoryId = _state.value.categories.findLast { it.name == name }?.id
    }

    fun setCity(name: String) {
        areFieldsFilled()
        currentCityId = _state.value.cities.findLast { it.name == name } ?.id
    }

    fun addLink(link: ExternalCompanyLink) {
        links[link.type] = link
    }

    fun createUpdateCompany() {
        if (areFieldsFilled()) {
            Log.d("CreateCompanyViewModel", "start company creation")
            viewModelScope.launch(Dispatchers.IO) {
                val params = CompanyCreationParams(
                    categoryId = currentCategoryId!!,
                    avatar = getCompanyAvatarOrNull(),
                    locale = locales.values.toList(),
                    links = links.values.toList(),
                    city = currentCityId!!
                )
                val result = if (companyId == null)
                    createCompany.createCompany(params)
                else updateCompany.updateCompany(params, companyId!!)
                result.onSuccess {
                    _events.trySend(CompanyCreationEvent.CloseSelf)
                    Log.d("CreateCompanyViewModel", "Company created !!")
                }.onFailure {
                    _events.trySend(CompanyCreationEvent.ErrorWhileCreatingCompany)
                    Log.e("CreateCompanyViewModel", it.message ?: "")
                }
            }
        } else {
            Log.d("CreateCompanyViewModel", "areFieldsFilled = false")
        }
    }

    private suspend fun getCompanyAvatarOrNull(): String?  {
        val localFile = _state.value.localFileAvatar
        return if (localFile == null) {
            null
        } else {
            uploadRepositoryImpl.partialFileUpload(File(localFile))
        }
    }

    private fun areFieldsFilled(): Boolean {
        val isLocalesFilled = locales.values.none { !it.isFilled() }
        val isEmptyCategory = currentCategoryId == null
        val errorCategory = if (isEmptyCategory) CreateCompanyFieldError.FIELD_EMPTY else null
        locales.values.firstOrNull()?.let { locale ->
            val isNameFiled = locale.name.isNotEmpty()
            val isDescriptionFilled = locale.description.isNotEmpty()
            _state.tryEmit(
                _state.value.copy(
                    errorCompanyName = if (isNameFiled) null else CreateCompanyFieldError.FIELD_EMPTY,
                    errorDescription = if (isDescriptionFilled) null else CreateCompanyFieldError.FIELD_EMPTY,
                    errorCategory = errorCategory
                )
            )
        } ?: run {
            _state.tryEmit(
                _state.value.copy(
                    errorCompanyName = CreateCompanyFieldError.FIELD_EMPTY,
                    errorDescription = CreateCompanyFieldError.FIELD_EMPTY,
                    errorCategory = errorCategory
                )
            )
        }
        return isLocalesFilled && currentCategoryId != null
    }

    fun setCompanyId(companyId: String?) {
        Log.d(
            "CreateCompanyViewModel",
            "setCompanyId = $companyId"
        )
        viewModelScope.launch(Dispatchers.Default) {
            val categories = getCategoriesUseCase.invoke().getOrNull() ?: emptyList()
            if(categories.isEmpty()){
                //TODO show error
                Log.d(
                    "CreateCompanyViewModel",
                    "error while getting categories"
                )
            }
            val cities = getCitiesUseCase.invoke().getOrNull() ?: emptyList()
            if(cities.isEmpty()){
                //TODO show error
                Log.d(
                    "CreateCompanyViewModel",
                    "error while getting cities"
                )
            }

            handleCategoriesAndCities(categories, cities)
            if (companyId.isNullOrEmpty()) {
                return@launch
            }
            this@CreateCompanyViewModel.companyId = companyId
            if (categories.isNullOrEmpty()) return@launch

            getEditCompanyInfo
                .getEditCompanyInfo(companyId)
                .onSuccess { model ->
                    Log.d(
                        "CreateCompanyViewModel",
                        "success ${model}"
                    )
                    this@CreateCompanyViewModel.currentCityId = model.cityId
                    this@CreateCompanyViewModel.currentCategoryId = model.categoryId
                    _events.send(
                        CompanyCreationEvent.CompanyInfoReceived(
                            model.copy(
                                categoryName = categories.findLast {
                                    it.categoryId == model.categoryId
                                }?.categoryName ?: "",
                                cityName = cities.findLast { it.cityId == model.cityId}?.cityName ?: ""
                            )
                        )
                    )
                    _state.emit(_state.value.copy(remoteAvatar = model.avatar))
                }
                .onFailure {
                    Log.e(
                        "CreateCompanyViewModel",
                        "getEditCompanyInfo error ${it.message}"
                    )
                    //TODO show error
                }

        }

    }

    fun setCompanyAvatar(file: String?) {
        file?.let {
            viewModelScope.launch(Dispatchers.Default) {
                _state.emit(_state.value.copy(localFileAvatar = file))
            }
        }
    }
}
package com.freebie.frieebiemobile.ui.company.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentCreateCompanyBinding
import com.freebie.frieebiemobile.network.DEFAULT_LOCALE
import com.freebie.frieebiemobile.permissions.PermissionManagerImpl
import com.freebie.frieebiemobile.permissions.PermissionResult
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyEditModel
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalCompanyLink
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalLinkType
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyCreationEvent
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyCreationUiModel
import com.freebie.frieebiemobile.ui.utils.gone
import com.freebie.frieebiemobile.ui.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CreateCompanyFragment : Fragment() {

    private var _binding: FragmentCreateCompanyBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<CreateCompanyViewModel>()

    @Inject
    lateinit var permissionManager: PermissionManagerImpl

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            viewModel.setCompanyAvatar(getRealPathFromURI(requireContext(), data))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCompanyId(arguments?.getString(COMPANY_ID))
        observeState()
        observeEvents()
        binding.createCompany.setOnClickListener {
            viewModel.createUpdateCompany()
        }
        addTextListeners()
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.addImage.setOnClickListener {
            requestImage()
        }
    }

    private fun addTextListeners() {
        binding.etCompanyDescription.doAfterTextChanged { text: Editable? ->
            addLocale()
        }

        binding.etCompanyName.doAfterTextChanged {
            addLocale()
        }

        binding.include.etTelegram.doAfterTextChanged {
            viewModel.addLink(ExternalCompanyLink(it.toString(), ExternalLinkType.TELEGRAM))
        }

        binding.include.etInstagram.doAfterTextChanged {
            viewModel.addLink(ExternalCompanyLink(it.toString(), ExternalLinkType.INSTAGRAM))
        }

        binding.include.etWhatsapp.doAfterTextChanged {
            viewModel.addLink(ExternalCompanyLink(it.toString(), ExternalLinkType.WHATSAPP))
        }
    }

    private fun addLocale() = viewModel.addLocale(
        langCode = resources.configuration.locales[0].language ?: DEFAULT_LOCALE,
        description = binding.etCompanyDescription.text.toString(),
        name = binding.etCompanyName.text.toString()
    )

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::handleState)
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect(::handleEvents)
            }
        }
    }

    private fun handleEvents(event: CompanyCreationEvent) {
        when (event) {
            CompanyCreationEvent.CloseSelf -> activity?.onBackPressedDispatcher?.onBackPressed()
            CompanyCreationEvent.ErrorWhileCreatingCompany -> Toast.makeText(
                requireContext(),
                "Error while creating company",
                Toast.LENGTH_SHORT
            ).show()

            is CompanyCreationEvent.CompanyInfoReceived -> updateFields(event.model)
            CompanyCreationEvent.ErrorWhileGettingCompanyInfo -> Toast.makeText(
                requireContext(),
                "Error while getting company info",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateFields(model: CompanyEditModel) {
        val name = model.locale.firstOrNull()?.name ?: ""
        val description = model.locale.firstOrNull()?.description ?: ""
        binding.etCompanyName.setText(name)
        binding.etCompanyDescription.setText(description)
        binding.cityAutoComplete.setText(model.cityName)
        binding.categoryAutoComplete.setText(model.categoryName)
        model.links.findLast { it.type == ExternalLinkType.WHATSAPP }?.let {
            binding.include.etWhatsapp.setText(it.url)
        }
        model.links.findLast { it.type == ExternalLinkType.TELEGRAM }?.let {
            binding.include.etTelegram.setText(it.url)
        }
        model.links.findLast { it.type == ExternalLinkType.INSTAGRAM }?.let {
            binding.include.etInstagram.setText(it.url)
        }
    }

    private fun handleState(state: CompanyCreationUiModel) {
        initDropDownMenuCategories(state.categories.map { it.name })
        initDropDownMenuCities(state.cities.map { it.name })
        handleAvatar(state)
        handleErrors(state)
    }

    private fun handleAvatar(state: CompanyCreationUiModel) {
        if (state.remoteAvatar.isNullOrEmpty() && state.localFileAvatar.isNullOrEmpty()) {
            binding.addImage.setText(R.string.add_company_avatar)
            binding.companyAvatar.gone()
        } else {
            binding.addImage.setText(R.string.change_company_avatar)
            binding.companyAvatar.visible()
            Glide.with(binding.companyAvatar)
                .load(state.localFileAvatar ?: state.remoteAvatar)
                .into(binding.companyAvatar)
        }
    }

    private fun handleErrors(state: CompanyCreationUiModel) {
        if (state.errorCompanyName?.resId != null) {
            binding.layoutCompanyName.isErrorEnabled = true
            binding.layoutCompanyName.error =
                context?.getString(state.errorCompanyName.resId) ?: ""
        } else {
            binding.layoutCompanyName.isErrorEnabled = false
        }

        if (state.errorDescription?.resId != null) {
            binding.layoutCompanyDescription.isErrorEnabled = true
            binding.layoutCompanyDescription.error =
                context?.getString(state.errorDescription.resId) ?: ""
        } else {
            binding.layoutCompanyDescription.isErrorEnabled = false
        }

        if (state.errorCategory?.resId != null) {
            binding.layoutCompanyCategory.isErrorEnabled = true
            binding.layoutCompanyCategory.error =
                context?.getString(state.errorCategory.resId) ?: ""
        } else {
            binding.layoutCompanyCategory.isErrorEnabled = false
        }
    }

    private fun initDropDownMenuCategories(items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_category_simple, items)
        binding.categoryAutoComplete.setAdapter(adapter)
        binding.categoryAutoComplete.setOnItemClickListener { parent, view, position, id ->
            viewModel.setCategory(items[position])
        }
    }

    private fun initDropDownMenuCities(items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_category_simple, items)
        binding.cityAutoComplete.setAdapter(adapter)
        binding.cityAutoComplete.setOnItemClickListener { parent, view, position, id ->
            viewModel.setCity(items[position])
        }
    }

    private fun requestImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            verifyStoragePermissions()
        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? { //todo move to utils
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    // Storage Permissions
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun verifyStoragePermissions() {
        // Check if we have write permission
        permissionManager.requestPermissions(Manifest.permission.READ_MEDIA_IMAGES) { result ->
            when (result) {
                PermissionResult.Granted -> {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startForResult.launch(galleryIntent)
                }

                else -> {
                    Toast.makeText(requireContext(), "Permissions required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val COMPANY_ID = "company_id"

        fun newInstance(companyId: String?): Fragment {
            val bundle = Bundle().apply {
                companyId?.let { putString(COMPANY_ID, it) }
            }
            return CreateCompanyFragment().apply {
                arguments = bundle
            }
        }
    }
}
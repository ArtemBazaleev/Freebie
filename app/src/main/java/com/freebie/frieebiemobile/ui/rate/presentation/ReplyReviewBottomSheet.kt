package com.freebie.frieebiemobile.ui.rate.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentReplyCommentBinding
import com.freebie.frieebiemobile.ui.rate.presentation.model.ReplyCompanyTransitModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.UserRateUiModel
import com.freebie.frieebiemobile.ui.utils.gone
import com.freebie.frieebiemobile.ui.utils.visible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class ReplyReviewBottomSheet : BottomSheetDialogFragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentReplyCommentBinding? = null

    private var successListener: WeakReference<(String) -> Unit>? = null

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReplyCommentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initView()
        return root
    }

    private fun initView() {
        binding.tvRate.setOnClickListener {
            val text = binding.reviewMessage.text.toString()
            successListener?.get()?.invoke(text)
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderComment()
    }

    private fun renderComment() {
        val userRateUiModel = arguments?.getParcelable<UserRateUiModel>(REVIEW_INFO) ?: error("UserRateUiModel not found")
        binding.userReview.reviewerName
        binding.userReview.reviewerName.text = userRateUiModel.reviewerName
        binding.userReview.reviewerComment.text = userRateUiModel.comment
        if (userRateUiModel.needToShowFullText) {
            binding.userReview.reviewerComment.maxLines = Int.MAX_VALUE
        } else {
            binding.userReview.reviewerComment.maxLines = 3
            binding.userReview.reviewerComment.setLines(3)
        }

        if (userRateUiModel.needToShowRating && userRateUiModel.reviewerRating != null) {
            binding.userReview.reviewScore.apply {
                visible()
                text = userRateUiModel.reviewerRating.toString()
            }
        } else binding.userReview.reviewScore.gone()

        Glide.with(requireContext())
            .load(userRateUiModel.avatar)
            .circleCrop()
            .into(binding.userReview.userAvatar)

        binding.userReview.reviewDate.text = userRateUiModel.date
    }

    private fun setSuccessListener(successListener: (String) -> Unit) {
        this.successListener = WeakReference(successListener)
    }

    companion object {
        private const val TAG = "RateCompanyBottomSheet"
        private const val REVIEW_INFO = "REVIEW_INFO"

        fun show(
            fm: FragmentManager,
            transitData: ReplyCompanyTransitModel,
            successListener: (String) -> Unit
        ) {
            if (fm.findFragmentByTag(TAG) != null) return
            val fragment = ReplyReviewBottomSheet()
            val bundle = Bundle().apply {
                putParcelable(REVIEW_INFO, transitData.userRate)
            }
            fragment.arguments = bundle
            fragment.setSuccessListener(successListener)
            fragment.show(fm, TAG)
        }
    }
}
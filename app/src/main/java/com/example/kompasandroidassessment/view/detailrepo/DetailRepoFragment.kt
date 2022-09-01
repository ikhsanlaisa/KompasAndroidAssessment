package com.example.kompasandroidassessment.view.detailrepo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kompasandroidassessment.R
import com.example.kompasandroidassessment.data.local.entity.DetailRepoEntity
import com.example.kompasandroidassessment.databinding.FragmentDetailRepoBinding
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setInvisible
import dagger.hilt.android.AndroidEntryPoint
import com.example.kompasandroidassessment.data.remote.Result
import com.example.kompasandroidassessment.utils.ChipLoader.addChip
import com.example.kompasandroidassessment.utils.CountFormatUtil.toCountFormat
import com.example.kompasandroidassessment.utils.DateFormatUtil.getTimeAgo
import com.example.kompasandroidassessment.utils.LanguageColorUtil.setLeftDrawableColor
import com.example.kompasandroidassessment.utils.SnackBarExt.showSnackBar
import com.example.kompasandroidassessment.utils.TextLoader.loadData
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setGone
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setVisible

@AndroidEntryPoint
class DetailRepoFragment : Fragment() {

    private var _binding: FragmentDetailRepoBinding? = null
    private val binding get() = _binding
    private val viewModel: DetailRepoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailRepoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(
            R.drawable.ic_baseline_arrow_back_24)
        setHasOptionsMenu(true)
        setUpView()
    }

    private fun setUpView() {
        val username = DetailRepoFragmentArgs.fromBundle(arguments as Bundle).username
        val repoName = DetailRepoFragmentArgs.fromBundle(arguments as Bundle).repositoryName
        if (username != null && repoName != null) {
            viewModel.setData(username, repoName)
            viewModel.setHasBeenHandled()
        }
        binding?.contentRepo?.root?.setInvisible()
        viewModel.getDetailRepository.observe(requireActivity(), observer)
    }

    private val observer = Observer<Result<DetailRepoEntity>> { result ->
        viewModel.isResultHasBeenHandled.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { resultHasNotBeenHandled ->
                if (resultHasNotBeenHandled) {
                    when (result) {
                        is Result.Success -> {
                            showContent()
                            result.data?.let { detailResult ->
                                populateUser(detailResult)
                            }
                        }
                        is Result.Error -> {
                            showContent()
                            requireActivity().showSnackBar(
                                requireActivity().window.decorView.rootView,
                                result.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showContent() = binding?.apply {
        shimmer.setGone()
        contentRepo.root.setVisible()
    }

    private fun populateUser(detailRepoEntity: DetailRepoEntity) {
        binding?.contentRepo?.apply {
            with(detailRepoEntity) {
                tvRepoName.loadData(name)
                repoUrl.loadData(fullName)
                tvLastUpdated.loadData(updatedAt.getTimeAgo().replace("Updated ", ""))
                tvStars.loadData(resources.getString(R.string.stars, stargazersCount?.toCountFormat()))
                tvForks.loadData(resources.getString(R.string.forks, forksCount?.toCountFormat()))
                tvLanguage.loadData(language)
                tvIssuesCount.loadData(openIssuesCount?.toCountFormat())
                tvWatchersCount.loadData(watchersCount?.toCountFormat())
                tvNetworkCount.loadData(networkCount?.toCountFormat())
                tvDescription.text = description ?: "No description provided."
                tvLanguage.setLeftDrawableColor(requireActivity(), language)
                topics?.forEach { cgTopics.addChip(requireActivity(), it) }
                repoUrl.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(htmlUrl))
                    startActivity(intent)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
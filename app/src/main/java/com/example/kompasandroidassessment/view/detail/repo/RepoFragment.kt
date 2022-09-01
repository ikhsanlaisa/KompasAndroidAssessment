package com.example.kompasandroidassessment.view.detail.repo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kompasandroidassessment.data.remote.Result
import com.example.kompasandroidassessment.data.local.entity.RepoEntity
import com.example.kompasandroidassessment.databinding.FragmentRepoBinding
import com.example.kompasandroidassessment.utils.NavControllerHelper.safeNavigate
import com.example.kompasandroidassessment.utils.SnackBarExt.showSnackBar
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setGone
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setVisible
import com.example.kompasandroidassessment.view.adapters.RepoAdapter
import com.example.kompasandroidassessment.view.detail.DetailFragment
import com.example.kompasandroidassessment.view.detail.DetailFragmentDirections
import com.example.kompasandroidassessment.view.detail.DetailViewModel
import com.example.kompasandroidassessment.view.detail.follow.FollowFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoFragment : Fragment(), RepoAdapter.OnRepoCallback {

    private var _binding: FragmentRepoBinding? = null
    private val binding get() = _binding
    private val viewModel: DetailViewModel by viewModels ()
    private lateinit var repoAdapter: RepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(FollowFragment.EXTRA_USERNAME)
        if (name != null) viewModel.setUsername(name)
        if (activity != null) {
            repoAdapter = RepoAdapter(this)
            viewModel.getRepository.observe(viewLifecycleOwner, observer)
            binding?.rvRepo?.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                setHasFixedSize(true)
                adapter = repoAdapter
            }
        }
    }

    private val observer = Observer<Result<List<RepoEntity>>> { result ->
        when (result) {
            is Result.Success -> {
                showContent()
                result.data?.let {
                    if (it.isNotEmpty()) repoAdapter.submitList(it) else
                        binding?.noRepo?.root?.setVisible()
                }
            }
            is Result.Error -> {
                showContent()
                with(requireActivity()) {
                    showSnackBar(this.window.decorView.rootView, result.message)
                }
            }
        }
    }

    private fun showContent() = binding?.apply {
        shimmer.root.setGone()
        rvRepo.setVisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(repoEntity: RepoEntity) {
        val toRepoDetail = DetailFragmentDirections.actionDetailFragmentToRepositoryFragment()
        toRepoDetail.username = repoEntity.owner
        toRepoDetail.repositoryName = repoEntity.name
        safeNavigate(toRepoDetail, DetailFragment::class.java.name)
    }
}
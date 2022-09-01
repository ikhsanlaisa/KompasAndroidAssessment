package com.example.kompasandroidassessment.view.onsearch

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kompasandroidassessment.R
import com.example.kompasandroidassessment.data.local.entity.UserEntity
import com.example.kompasandroidassessment.databinding.FragmentOnSearchBinding
import com.example.kompasandroidassessment.view.MainViewModel
import com.example.kompasandroidassessment.view.adapters.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.example.kompasandroidassessment.data.remote.Result
import com.example.kompasandroidassessment.utils.NavControllerHelper.safeNavigate
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setGone
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setInvisible
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setVisible

@AndroidEntryPoint
class OnSearchFragment : Fragment(), UserAdapter.UserClickCallback, SearchView.OnQueryTextListener {

    private var _binding: FragmentOnSearchBinding? = null
    private val binding get() = _binding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(activity as AppCompatActivity) {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }
        setHasOptionsMenu(true)
        setUpView()
    }

    private val observer = Observer<Result<List<UserEntity>>> { result ->
        when(result) {
            is Result.Success -> {
                showContent()
                result.data?.let {
                    userAdapter.submitList(it)
                }
            }
            is Result.Error -> {
                showContent()
            }
        }
    }

    private fun setUpView() {
        userAdapter = UserAdapter(this)
        binding?.apply {
            this@OnSearchFragment.searchView = searchView
            rvMain.layoutManager = LinearLayoutManager(requireActivity())
            rvMain.setHasFixedSize(true)
            rvMain.adapter = userAdapter
        }
        showContent()
        setUpSearchView()
    }

    private fun setUpSearchView() {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding?.searchView?.apply {
            if (viewModel.searchUser.value == null) requestFocus()
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(this@OnSearchFragment)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            hideContent()
            viewModel.setQuery(query)
            viewModel.searchUser.observe(viewLifecycleOwner, observer)
        }
        searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null && newText.isNotEmpty()) {
            hideContent()
            viewModel.setQuery(newText)
            viewModel.searchUser.observe(viewLifecycleOwner, observer)
        }
        return true
    }

    private fun showContent() = binding?.apply {
        shimmer.root.setGone()
        rvMain.setVisible()
    }

    private fun hideContent() = binding?.apply {
        shimmer.root.setVisible()
        rvMain.setInvisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onItemClicked(user: UserEntity) {
        val toDetail = OnSearchFragmentDirections.actionOnSearchFragmentToDetailFragment()
        toDetail.username = user.login
        safeNavigate(toDetail, javaClass.name)
    }
}
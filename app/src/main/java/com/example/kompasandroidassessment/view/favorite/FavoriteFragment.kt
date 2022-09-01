package com.example.kompasandroidassessment.view.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kompasandroidassessment.R
import com.example.kompasandroidassessment.data.local.entity.DetailUserEntity
import com.example.kompasandroidassessment.databinding.FragmentFavoriteBinding
import com.example.kompasandroidassessment.utils.NavControllerHelper.safeNavigate
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setGone
import com.example.kompasandroidassessment.utils.ViewVisibilityUtil.setVisible
import com.example.kompasandroidassessment.view.MainViewModel
import com.example.kompasandroidassessment.view.adapters.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteAdapter.OnUserFavCallback, Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var favAdapter: FavoriteAdapter
    private var isDarkMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favAdapter = FavoriteAdapter(this)
        viewModel.getFavUsers().observe(viewLifecycleOwner, observer)
    }

    private val observer = Observer<List<DetailUserEntity>> {
        binding?.shimmer?.root?.setGone()
        favAdapter.submitList(it)
        if (it.isEmpty()) binding?.noUsers?.setVisible()
        binding?.rvFav?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = favAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(user: DetailUserEntity) {
        val toDetail = FavoriteFragmentDirections.actionFavoriteToDetailFragment()
        toDetail.username = user.login
        safeNavigate(toDetail, javaClass.name)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }
}
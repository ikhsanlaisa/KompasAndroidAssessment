package com.example.kompasandroidassessment.view

import androidx.lifecycle.*
import com.example.kompasandroidassessment.data.local.entity.DetailUserEntity
import com.example.kompasandroidassessment.data.local.entity.UserEntity
import com.example.kompasandroidassessment.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.kompasandroidassessment.data.remote.Result

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val query = MutableLiveData<String>()

    fun setQuery(query: String) {
        this.query.value = query
    }

    val dataUser = userRepository.getUsers()

    val searchUser: LiveData<Result<List<UserEntity>>> = Transformations.switchMap(query) {
        userRepository.searchUser(it)
    }

    fun getFavUsers(): LiveData<List<DetailUserEntity>> = userRepository.getFavoriteUsers()
}
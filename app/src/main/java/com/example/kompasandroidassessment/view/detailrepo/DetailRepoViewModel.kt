package com.example.kompasandroidassessment.view.detailrepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.kompasandroidassessment.data.local.entity.DetailRepoEntity
import com.example.kompasandroidassessment.data.repository.UserRepository
import com.example.kompasandroidassessment.utils.DoubleTrigger
import com.example.kompasandroidassessment.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.kompasandroidassessment.data.remote.Result

@HiltViewModel
class DetailRepoViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private val username = MutableLiveData<String>()
    private val repositoryName = MutableLiveData<String>()
    private val _isResultHasBeenHandled = MutableLiveData<Event<Boolean>>()
    val isResultHasBeenHandled: LiveData<Event<Boolean>> get() = _isResultHasBeenHandled

    fun setData(username: String, repositoryName: String) {
        this.username.value = username
        this.repositoryName.value = repositoryName
    }

    val getDetailRepository: LiveData<Result<DetailRepoEntity>> = Transformations.switchMap(DoubleTrigger(username, repositoryName)
    ) {
        userRepository.getDetailRepo(it.first.toString(), it.second.toString())
    }

    fun setHasBeenHandled() {
        _isResultHasBeenHandled.value = Event(true)
    }
}
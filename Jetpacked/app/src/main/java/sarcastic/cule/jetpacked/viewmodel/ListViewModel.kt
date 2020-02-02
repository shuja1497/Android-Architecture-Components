package sarcastic.cule.jetpacked.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import sarcastic.cule.jetpacked.model.DogBreed
import sarcastic.cule.jetpacked.model.DogsApiService

class ListViewModel : ViewModel() {

    private val dogsApiService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refreshList() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {

        loading.value = true

        disposable.add(
            dogsApiService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogsList: List<DogBreed>) {
                        dogs.value = dogsList
                        loading.value = false
                        loadError.value = false
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        loadError.value = true
                        e.printStackTrace()
                    }
                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}
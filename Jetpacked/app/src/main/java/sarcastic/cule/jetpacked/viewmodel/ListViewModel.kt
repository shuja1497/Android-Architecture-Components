package sarcastic.cule.jetpacked.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import sarcastic.cule.jetpacked.model.DogBreed
import sarcastic.cule.jetpacked.model.DogDatabase
import sarcastic.cule.jetpacked.model.DogsApiService
import sarcastic.cule.jetpacked.utils.SharedPreferenceHelper

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val dogsApiService = DogsApiService()
    private val disposable = CompositeDisposable()
    private val sharedPreferenceHelper = SharedPreferenceHelper(getApplication())

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
                        storeDogsLocally(dogsList)
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        loadError.value = true
                        e.printStackTrace()
                    }
                })
        )

    }

    fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()

            dao.deleteAllDogs()
            val result = dao.insertAllDogs(*list.toTypedArray())

            var i = 0
            while (i < list.size) {

                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(list)
        }

        sharedPreferenceHelper.saveUpdateTime(System.nanoTime())
    }

    private fun dogsRetrieved(dogsList: List<DogBreed>) {
        dogs.value = dogsList
        loading.value = false
        loadError.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}
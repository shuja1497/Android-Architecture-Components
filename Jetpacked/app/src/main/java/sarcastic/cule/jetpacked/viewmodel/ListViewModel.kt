package sarcastic.cule.jetpacked.viewmodel

import android.app.Application
import android.widget.Toast
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
import sarcastic.cule.jetpacked.utils.NotificationHelper
import sarcastic.cule.jetpacked.utils.SharedPreferenceHelper

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val dogsApiService = DogsApiService()
    private val disposable = CompositeDisposable()
    private val sharedPreferenceHelper = SharedPreferenceHelper(getApplication())
    private val refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val dogs = MutableLiveData<List<DogBreed>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refreshList() {

        val updateTime = sharedPreferenceHelper.getUpdateTime()

        if (updateTime != null && updateTime != 0L && (System.nanoTime() - updateTime) < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    fun refreshByPassCache() {
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {

        loading.value = true

        launch {
            val dogList = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogList)
            Toast.makeText(getApplication(), "Fetched from database", Toast.LENGTH_SHORT).show()
        }
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
                        Toast.makeText(getApplication(), "Fetched from endpoint", Toast.LENGTH_SHORT).show()
                        NotificationHelper(getApplication()).createNotification()
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
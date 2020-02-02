package sarcastic.cule.jetpacked.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sarcastic.cule.jetpacked.model.DogBreed

class ListViewModel : ViewModel() {

    val dogs = MutableLiveData<List<DogBreed>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refreshList() {

        val dog1 = DogBreed("1", "breed 1", "10 yrs", "","","","")
        val dog2 = DogBreed("2", "breed 2", "20 yrs", "","","","")
        val dog3 = DogBreed("3", "breed 3", "30 yrs", "","","","")
        val dogDummyList = arrayListOf(dog1, dog2, dog3)

        dogs.value = dogDummyList
        loadError.value = false
        loading.value = false

    }

}
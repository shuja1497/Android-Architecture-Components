package sarcastic.cule.jetpacked.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sarcastic.cule.jetpacked.model.DogBreed

class DetailViewModel : ViewModel() {

    val dogBreed = MutableLiveData<DogBreed>()

    fun getDogBreed() {

        val dogBreedDummy = DogBreed("breedId","breedName", "10 years", "group A",
            "bredFor", "temperament", "")
        dogBreed.value = dogBreedDummy
    }

}
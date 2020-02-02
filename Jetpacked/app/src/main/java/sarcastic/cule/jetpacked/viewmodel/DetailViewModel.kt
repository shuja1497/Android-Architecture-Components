package sarcastic.cule.jetpacked.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import sarcastic.cule.jetpacked.model.DogBreed
import sarcastic.cule.jetpacked.model.DogDatabase

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dogBreed = MutableLiveData<DogBreed>()

    fun getDogBreed(uuid: Int) {
        launch {
            dogBreed.value = DogDatabase(getApplication()).dogDao().getDog(uuid)
        }
    }
}
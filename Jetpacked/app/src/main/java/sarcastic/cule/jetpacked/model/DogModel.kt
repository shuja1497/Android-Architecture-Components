package sarcastic.cule.jetpacked.model

import com.google.gson.annotations.SerializedName

data class DogBreed(

    @SerializedName("id")
    val breedId: String?,

    @SerializedName("name")
    val breed: String,

    @SerializedName("life_span")
    val lifespan: String?,

    @SerializedName("breed_group")
    val breedGroup: String?,

    @SerializedName("bred_for")
    val bredFor: String?,

    @SerializedName("temperament")
    val temperament: String?,

    @SerializedName("url")
    val imageUrl: String?
)
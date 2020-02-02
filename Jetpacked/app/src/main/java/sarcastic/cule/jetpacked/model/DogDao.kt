package sarcastic.cule.jetpacked.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DogDao {

    @Insert
    suspend fun insertAllDogs(vararg dogBreed: DogBreed): List<Long>

    @Query("select * from dogbreed")
    suspend fun getAllDogs() : List<DogBreed>

    @Query("select * from dogbreed where uuid = :dogId")
    suspend fun getDog(dogId: Int) : DogBreed

    @Query("delete from dogbreed")
    suspend fun deleteAllDogs()

}
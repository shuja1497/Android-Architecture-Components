package sarcastic.cule.jetpacked.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import sarcastic.cule.jetpacked.R
import sarcastic.cule.jetpacked.model.DogBreed
import sarcastic.cule.jetpacked.utils.getProgressDrawable
import sarcastic.cule.jetpacked.utils.loadImage
import sarcastic.cule.jetpacked.view.ListFragmentDirections

class DogsListAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

    fun updateDogList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        holder.view.title.text = dogsList[position].breed
        holder.view.subtitle.text = dogsList[position].lifespan
        holder.view.image.loadImage(dogsList[position].imageUrl, getProgressDrawable(holder.view.context))
        holder.view.setOnClickListener {
            Navigation.findNavController(it).navigate(ListFragmentDirections.actionListFragmentToDetailFragment())
        }
    }

    class DogViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
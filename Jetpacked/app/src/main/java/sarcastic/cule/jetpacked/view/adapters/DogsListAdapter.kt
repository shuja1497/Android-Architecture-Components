package sarcastic.cule.jetpacked.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import sarcastic.cule.jetpacked.R
import sarcastic.cule.jetpacked.databinding.ListItemBinding
import sarcastic.cule.jetpacked.model.DogBreed
import sarcastic.cule.jetpacked.utils.getProgressDrawable
import sarcastic.cule.jetpacked.utils.loadImage
import sarcastic.cule.jetpacked.view.DogClickListener
import sarcastic.cule.jetpacked.view.ListFragmentDirections

class DogsListAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener {

    fun updateDogList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ListItemBinding>(inflater, R.layout.list_item, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        // attaching variables
        holder.view.dog = dogsList[position]
        holder.view.listener = this
    }

    override fun onDogClick(v: View) {
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        action.uuid = v.dogId.text.toString().toInt()
        Navigation.findNavController(v).navigate(action)
    }

    class DogViewHolder(val view: ListItemBinding) : RecyclerView.ViewHolder(view.root)
}
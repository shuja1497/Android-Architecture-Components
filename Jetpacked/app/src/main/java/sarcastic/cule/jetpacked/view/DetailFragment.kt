package sarcastic.cule.jetpacked.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*

import sarcastic.cule.jetpacked.R
import sarcastic.cule.jetpacked.viewmodel.DetailViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private var uuid = 0
    private lateinit var detailViewModel : DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            uuid = DetailFragmentArgs.fromBundle(it).uuid
        }

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.getDogBreed()

        observeDetailedViewModel()

    }

    private fun observeDetailedViewModel() {

        detailViewModel.dogBreed.observe(this, Observer {

            it?.let { dog ->
                name.text = dog.breed
                lifespan.text = dog.lifespan
                temperament.text = dog.temperament
                purpose.text = dog.bredFor
            }
        })
    }

}

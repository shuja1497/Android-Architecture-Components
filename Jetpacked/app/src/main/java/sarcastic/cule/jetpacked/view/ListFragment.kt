package sarcastic.cule.jetpacked.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*

import sarcastic.cule.jetpacked.R
import sarcastic.cule.jetpacked.view.adapters.DogsListAdapter
import sarcastic.cule.jetpacked.viewmodel.ListViewModel

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val listAdapter = DogsListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java) // instantiating a view model
        viewModel.refreshList()

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        refresh_layout.setOnRefreshListener {
            list.visibility = View.GONE
            loader.visibility = View.GONE
            error.visibility = View.GONE
            refresh_layout.isRefreshing = false
            viewModel.refreshByPassCache()
        }

        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.dogs.observe(this, Observer { dogs ->
             dogs?.let {
                 list.visibility = View.VISIBLE
                 listAdapter.updateDogList(dogs)
             }
        })

        viewModel.loadError.observe(this, Observer {isError->

            isError?.let {
                error.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading->
            isLoading?.let {
                loader.visibility = if (it) View.VISIBLE else View.GONE

                if (it) {
                    error.visibility = View.GONE
                    list.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_settings -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(ListFragmentDirections.actionListFragmentToSettingsFragment())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

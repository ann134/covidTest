package com.test.covid19test.ui.mainscreen

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.test.covid19test.R
import com.test.covid19test.api.model.Brief
import com.test.covid19test.api.model.Country
import com.test.covid19test.ui.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var viewModel: MainViewModel
    private var briefDisposable = CompositeDisposable()
    lateinit var adapter: CountryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CountryListAdapter { partItem: Country ->
            onCountryClicked(partItem)
        }
        recycler.adapter = adapter

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(search_view.query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    override fun onStop() {
        super.onStop()
        briefDisposable.clear()
    }

    private fun loadData() {
        briefDisposable.add(
            viewModel.getBrief()
                .subscribe({ result ->
                    showBrief(result)
                }, { error ->
                    error.printStackTrace()
                })
        )

        briefDisposable.add(
            viewModel.getLatestByCountries()
                .subscribe({ result ->
                    showList(result)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }

    fun search(qwery: String) {
        briefDisposable.add(
            viewModel.getLatestByCountries(qwery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    showList(result)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }

    fun showBrief(brief: Brief) {
        tv_confirmed_value.text = brief.confirmed.toString()
        tv_recovered_value.text = brief.recovered.toString()
        tv_deaths_value.text = brief.deaths.toString()
    }

    private fun showList(list: ArrayList<Country>) {
        adapter.list = list
    }

    fun onCountryClicked(country: Country) {
        val bundle = Bundle()
        bundle.putParcelable("country", country)
        Navigation.findNavController(tv_title)
            .navigate(R.id.action_mainFragment_to_countryFragment, bundle)
    }
}
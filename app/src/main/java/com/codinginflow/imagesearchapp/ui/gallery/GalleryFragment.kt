package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.unsplash_photo_load_state_footer.*

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery){

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashPhotoAdapter()

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter{adapter.retry()},
                footer = UnsplashPhotoLoadStateAdapter{adapter.retry()},
            )
            btRetry.setOnClickListener {
                adapter.retry()
            }


        }

        viewModel.photos.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        }
        setHasOptionsMenu(true)

        adapter.addLoadStateListener {
            binding.apply {
                progressBar.isVisible = it.source.refresh is LoadState.Loading
                recyclerView.isVisible = it.source.refresh is LoadState.NotLoading
                btRetry.isVisible = it.source.refresh is LoadState.Error
                tvCannotLoad.isVisible = it.source.refresh is LoadState.Error
                if(it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && adapter.itemCount<1){
                    recyclerView.isVisible = false
                    tvNoResult.isVisible = true
                }else{
                    tvNoResult.isVisible = false

                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_galler,menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if(query!=null){
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
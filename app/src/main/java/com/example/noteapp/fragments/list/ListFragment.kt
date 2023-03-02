package com.example.noteapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteData
import com.example.noteapp.data.viewmodel.NoteViewModel
import com.example.noteapp.data.viewmodel.SharedViewModel
import com.example.noteapp.databinding.FragmentListBinding
import com.example.noteapp.fragments.list.adapter.ListAdapter
import com.example.noteapp.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar


class ListFragment : Fragment(R.layout.fragment_list), SearchView.OnQueryTextListener {

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mNoteViewModel: NoteViewModel by viewModels()
    private val listAdapter: ListAdapter = ListAdapter()
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

        hideKeyboard(requireActivity())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }


        setupMenu()

        setupRecyclerView()
        mNoteViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            listAdapter.submitList(data)
        })

        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseView(it)
        })


    }

    private fun showEmptyDatabaseView(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete_all -> confirmRemoval()
                    R.id.menu_priority_high -> mNoteViewModel.sortByHighPriority.observe(requireActivity(),Observer{
                        listAdapter.submitList(it)
                    })
                    R.id.menu_priority_low -> mNoteViewModel.sortByLowPriority.observe(requireActivity(),Observer{
                        listAdapter.submitList(it)
                    })
                }

                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = listAdapter
        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val itemToDelete = listAdapter.currentList[viewHolder.adapterPosition]
                mNoteViewModel.deleteItem(itemToDelete)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                restoreDeletedData(viewHolder.itemView, itemToDelete, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: NoteData, position: Int) {
        val snackbar = Snackbar.make(
            view, "Deleted '${deletedItem.title}", Snackbar.LENGTH_LONG
        )

        snackbar.setAction("Undo") {
            mNoteViewModel.insertData(deletedItem)
        }
        snackbar.show()
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mNoteViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully Removed Everything", Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove Everything?")
        builder.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        mNoteViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                listAdapter.submitList(it)
            }
        })
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }


}



package com.example.noteapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.viewmodel.NoteViewModel
import com.example.noteapp.data.viewmodel.SharedViewModel
import com.example.noteapp.databinding.FragmentListBinding
import com.example.noteapp.fragments.list.adapter.ListAdapter


class ListFragment : Fragment(R.layout.fragment_list) {

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
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete_all -> confirmRemoval()
                }

                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = listAdapter
        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val itemToDelete = listAdapter.currentList[viewHolder.adapterPosition]
                mNoteViewModel.deleteItem(itemToDelete)
                Toast.makeText(
                    requireContext(),
                    "Successfully Removed : '${itemToDelete.title}'",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
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


}



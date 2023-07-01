package com.example.tokokelontongapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tokokelontongapp.R
import com.example.tokokelontongapp.application.GroceriesApp
import com.example.tokokelontongapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var applicationContext: Context
    private val groceriesViewModel: GroceriesViewModel by viewModels {
        GroceriesViewModelFactory((applicationContext as GroceriesApp).repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroceriesListAdapter{groceries ->
            //ini list bisa diklik dan dapat data groceries jadi tidak null
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(groceries)
            findNavController().navigate(action)
        }
        binding.dataRecyclerView.adapter = adapter
        binding.dataRecyclerView.layoutManager = LinearLayoutManager(context)
        groceriesViewModel.allGrocerieses.observe(viewLifecycleOwner) {grocerieses ->
            grocerieses.let {
                if (grocerieses.isEmpty()){
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.illustrationImageView.visibility = View.VISIBLE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                    binding.illustrationImageView.visibility = View.GONE

                }
                adapter.submitList(grocerieses)
            }
        }

        binding.addFAB.setOnClickListener {
            //ini buton tambah jadi groceries pasti null
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(null)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.tokokelontongapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tokokelontongapp.R
import com.example.tokokelontongapp.application.GroceriesApp
import com.example.tokokelontongapp.databinding.FragmentSecondBinding
import com.example.tokokelontongapp.model.Groceries

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val groceriesViewModel: GroceriesViewModel by viewModels {
        GroceriesViewModelFactory((applicationContext as GroceriesApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var groceries: Groceries? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groceries = args.groceries
        //jika groceries null maka tampilan default nambah toko kelontong
        // jika groceries tidak null tampilan berubah
        if (groceries != null) {
            binding.deleteButon.visibility = View.VISIBLE
            binding.saveButon.text = "Rubah"
            binding.nameEditText.setText(groceries?.name)
            binding.addressEditText.setText(groceries?.address)
            binding.numberEditText.setText(groceries?.number)
        }
        val name = binding.nameEditText.text
        val address = binding.addressEditText.text
        val number= binding.numberEditText.text
        binding.saveButon.setOnClickListener {
            if (name.isEmpty()) {
                Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if (address.isEmpty()) {
                Toast.makeText(context, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                if (groceries == null){
                    val groceries = Groceries(0, name.toString(), address.toString(), number.toString())
                    groceriesViewModel.insert(groceries)
                } else {
                    val groceries = Groceries(groceries?.id!!, name.toString(), address.toString(), number.toString())
                    groceriesViewModel.update(groceries)
                }
                findNavController().popBackStack() // dismiss halaman
            }
        }

        binding.deleteButon.setOnClickListener {
            groceries?.let { groceriesViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
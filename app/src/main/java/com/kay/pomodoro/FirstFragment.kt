package com.kay.pomodoro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kay.pomodoro.databinding.FragmentFirstBinding
import com.kay.pomodoro.viewModels.PomodoroViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val viewModel:PomodoroViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when(checkedId){
                R.id.btn_min_25 -> {
                    viewModel.updateTimeSelection(1)
                }
                R.id.btn_min_50 -> {
                    viewModel.updateTimeSelection(2)
                }
                R.id.btn_min_90 -> {
                    viewModel.updateTimeSelection(3)
                }
            }
        }

        binding.buttonFirst.setOnClickListener {
          //  findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
          //  viewModel.startTimer()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
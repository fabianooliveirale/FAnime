package com.example.home.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.example.home.R
import com.example.home.databinding.FragmentSearchAnimeBinding
import com.example.network.NetworkResources
import com.example.screen_resources.BaseFragment
import com.example.screen_resources.extensions.onTextChanged
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SearchAnimeFragment : BaseFragment() {

    private var _binding: FragmentSearchAnimeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<SearchAnimeViewModel>()
    private var adapter: SearchAnimeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFocusEditText()
        initEditTextWatcher()
        initLiveData()
        adapter = SearchAnimeAdapter(viewModel.getBaseImageUrl()) { animeId ->
            viewModel.getRouter().goToAnimeDetails(binding.root, animeId)
        }
        binding.recyclerView.adapter = adapter
        binding.clearButton.setOnClickListener {
            binding.editText.text.clear()
            setViewEmptyState()
        }
    }

    private fun initLiveData() {
        viewModel.animesCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResources.Loading -> {
                    viewModel.getShowLoading().showLoading()
                }
                is NetworkResources.Succeeded -> {
                    viewModel.getShowLoading().hideLoading()
                    if (binding.editText.text.isEmpty()) return@observe
                    viewModel.animesCategoryData = it.data
                    adapter?.replace(ArrayList(it.data))

                    if(ArrayList(it.data).isNotEmpty()) {
                        binding.messageImageView.isGone = true
                    } else {
                        binding.messageImageView.isGone = false
                        setViewErrorState()
                    }
                }
                is NetworkResources.Failure -> {
                    binding.messageImageView.isGone = false
                    setViewErrorState()
                    viewModel.getShowLoading().hideLoading()
                }
            }
        }
    }


    private fun setViewEmptyState() {
        binding.apply {
            messageImageView.text = "Procure um anime"
            messageImageView.image = ContextCompat.getDrawable(requireContext(), com.example.screen_resources.R.drawable.toy_anya)
        }
    }

    private fun setViewErrorState() {
        binding.apply {
            messageImageView.text = "Nenhum anime encontrado"
            messageImageView.image = ContextCompat.getDrawable(requireContext(), com.example.screen_resources.R.drawable.anya)
        }
    }

    private fun initEditTextWatcher() {
        binding.editText.onTextChanged(viewModel.textSearchChange)
        binding.editText.onTextChanged {
            binding.clearButton.isGone = it.isEmpty()
            if (it == "") {
                setViewEmptyState()
                adapter?.clear()
            }
        }
    }

    private fun initFocusEditText() {
        binding.editText.isFocusableInTouchMode = true
        binding.editText.requestFocus()

        binding.editText.post {
            showKeyboard(binding.editText, this.requireContext())
        }
    }

    private fun showKeyboard(mEtSearch: EditText, context: Context) {
        mEtSearch.requestFocus()
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEtSearch, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
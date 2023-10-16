package com.zhari.farhanretrofit.presentation.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhari.farhanretrofit.R
import com.zhari.farhanretrofit.data.network.datasource.ProductDataSourceImpl
import com.zhari.farhanretrofit.data.network.service.ProductService
import com.zhari.farhanretrofit.data.repository.ProductRepository
import com.zhari.farhanretrofit.data.repository.ProductRepositoryImpl
import com.zhari.farhanretrofit.databinding.ActivityProductBinding
import com.zhari.farhanretrofit.utils.GenericViewModelFactory
import com.zhari.farhanretrofit.utils.proceedWhen

class ProductActivity : AppCompatActivity() {

    private val binding : ActivityProductBinding by lazy {
        ActivityProductBinding.inflate(layoutInflater)
    }

    private val adapterProduct = ProductListAdapter()

    private val viewModel : ProductViewModel by viewModels {
        val service = ProductService.invoke()
        val dataSource = ProductDataSourceImpl(service)
        val repo : ProductRepository = ProductRepositoryImpl(dataSource)
        GenericViewModelFactory.create(ProductViewModel(repo))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRvListProduct()
        observeData()
        getData()
    }

    private fun setupRvListProduct() {
        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(this@ProductActivity)
            adapter = adapterProduct
            adapterProduct.refreshList()
        }
    }

    private fun observeData() {
        viewModel.productList.observe(this){
            it.proceedWhen(
                doOnSuccess = {
                    binding.rvProduct.isVisible = true
                    binding.commonLayoutState.root.isVisible = false
                    binding.commonLayoutState.loading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = false
                    binding.rvProduct.apply {
                        layoutManager = LinearLayoutManager(this@ProductActivity)
                        adapter = adapterProduct
                    }
                    it.payload?.let {
                        adapterProduct.setData(it.products)
                    }
                },
                doOnLoading = {
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.loading.isVisible = true
                    binding.commonLayoutState.tvError.isVisible = false
                    binding.rvProduct.isVisible = false
                },
                doOnError = { err ->
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.loading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = true
                    binding.commonLayoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.rvProduct.isVisible = false
                },
                doOnEmpty = {
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.loading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = true
                    binding.commonLayoutState.tvError.text = getString(R.string.text_empty)
                    binding.rvProduct.isVisible = false
                }
            )
        }
    }

    private fun getData() {
        viewModel.getProductList()
    }

}
package com.zhari.farhanretrofit.presentation.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhari.farhanretrofit.data.model.ProductsResponse
import com.zhari.farhanretrofit.data.repository.ProductRepository
import com.zhari.farhanretrofit.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepo: ProductRepository
) : ViewModel(){

    val productList = MutableLiveData<ResultWrapper<ProductsResponse>>()

    fun getProductList(){
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.getProducts().collect{
                productList.postValue(it)
            }
        }
    }

}
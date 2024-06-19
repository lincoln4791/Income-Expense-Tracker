package com.lincoln4791.dailyexpensemanager.modelClass.QuoteModelClass

data class QuotesResponseModel(
    val count: Int,
    val lastItemIndex: Int,
    val page: Int,
    val results: List<Result>,
    val totalCount: Int,
    val totalPages: Int
)
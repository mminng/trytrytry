package com.ming.trycompose

import com.google.gson.annotations.SerializedName

/**
 * Created by zh on 2021/8/17.
 */
data class GeeksResponse(
    var showProgress: Boolean = false,
    var showError: String? = "",
    @field:SerializedName("issueList") var geeksList: List<GeeksModel>? = null
)

data class GeeksModel(
    @field:SerializedName("itemList") val bannerList: List<BannerListModel>
)

data class BannerListModel(
    @field:SerializedName("data") val data: BannerModel
)

data class BannerModel(
    @field:SerializedName("cover") val cover: CoverModel
)

data class CoverModel(
    @field:SerializedName("feed") val feed: String
)

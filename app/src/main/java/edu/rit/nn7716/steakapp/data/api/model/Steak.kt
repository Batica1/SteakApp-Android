package edu.rit.nn7716.steakapp.data.api.model

import com.google.gson.annotations.SerializedName

data class Steak(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("flavorProfiles")
    val flavorProfiles: String,
    @SerializedName("recommendedCookingMethods")
    val recommendedCookingMethods: String,
    @SerializedName("complementarySideDishes")
    val complementarySideDishes: String,
    @SerializedName("sauces")
    val sauces: String,
    @SerializedName("winePairings")
    val winePairings: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("isFavourite")
    var isFavourite: Boolean = false
)

data class SteaksResponse(
    @SerializedName("steaks")
    val steaks: List<Steak>
)

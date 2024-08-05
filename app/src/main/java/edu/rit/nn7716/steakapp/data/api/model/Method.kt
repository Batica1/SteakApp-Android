import com.google.gson.annotations.SerializedName

data class Method(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("video")
    val video: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("step1")
    val step1: String,
    @SerializedName("step2")
    val step2: String,
    @SerializedName("step3")
    val step3: String,
    @SerializedName("step4")
    val step4: String
)

data class MethodsResponse(
    @SerializedName("methods")
    val methods: List<Method>
)

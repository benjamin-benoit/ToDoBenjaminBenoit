
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Task(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String = "no description."
): Serializable, Parcelable

/*@Parcelize
data class Task(var id: String, var title: String, var description: String = "no description."): Serializable,
 Parcelable {} */
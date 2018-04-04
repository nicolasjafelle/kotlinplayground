package test.nicolasjafelle.kithub.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class Repo(val id: Long,
                val name: String = "",
                @SerializedName("full_name") val fullName: String,
                val owner: Owner,
                val private: Boolean,
                val fork: Boolean,
                val description: String,
                @SerializedName("html_url") val htmlUrl: String,
                @SerializedName("created_at") val createdAt: String,
                @SerializedName("updated_at") val updatedAt: String,
                val language: String? = "",
                @SerializedName("open_issues") val openIssues: Int,
                val forks: Int,
                val watchers: Int) : Parcelable
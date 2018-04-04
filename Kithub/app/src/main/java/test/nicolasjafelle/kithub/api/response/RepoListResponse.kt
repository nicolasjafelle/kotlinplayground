package test.nicolasjafelle.kithub.api.response

import com.google.gson.annotations.SerializedName
import test.nicolasjafelle.kithub.domain.Repo

data class RepoListResponse(@SerializedName("total_count") val totalCount: Int,
                            @SerializedName("incomplete_results") val incompleteResults: Boolean,
                            @SerializedName("items") val repoList: List<Repo>)
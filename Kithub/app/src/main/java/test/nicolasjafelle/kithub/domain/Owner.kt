package test.nicolasjafelle.kithub.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
"owner": {
    "login": "google",
    "id": 1342004,
    "avatar_url": "https://avatars1.githubusercontent.com/u/1342004?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/google",
    "html_url": "https://github.com/google",
    "followers_url": "https://api.github.com/users/google/followers",
    "following_url": "https://api.github.com/users/google/following{/other_user}",
    "gists_url": "https://api.github.com/users/google/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/google/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/google/subscriptions",
    "organizations_url": "https://api.github.com/users/google/orgs",
    "repos_url": "https://api.github.com/users/google/repos",
    "events_url": "https://api.github.com/users/google/events{/privacy}",
    "received_events_url": "https://api.github.com/users/google/received_events",
    "type": "Organization",
    "site_admin": false
}
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class Owner(val login: String,
                 val id: Long,
                 @SerializedName("avatar_url") val avatarUrl: String?,
                 val url: String,
                 @SerializedName("html_url") val htmlUrl: String,
                 @SerializedName("followers_url") val followersUrl: String,
                 @SerializedName("gists_url") val gistsUrl: String,
                 @SerializedName("starred_url") val starredUrl: String,
                 @SerializedName("subscriptions_url") val subscriptionsUrl: String,
                 @SerializedName("organizations_url") val organizationsUrl: String,
                 @SerializedName("repos_url") val reposUrl: String,
                 @SerializedName("events_url") val eventsUrl: String,
                 @SerializedName("received_events_url") val receivedEventsUrl: String,
                 @SerializedName("site_admin") val siteAdmin: Boolean) : Parcelable
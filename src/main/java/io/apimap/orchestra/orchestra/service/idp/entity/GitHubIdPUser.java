/*
Copyright 2021-2023 TELENOR NORGE AS

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package io.apimap.orchestra.orchestra.service.idp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitHubIdPUser{

    @JsonProperty("login")
    protected String login;

    @JsonProperty("id")
    protected Integer id;

    @JsonProperty("node_id")
    protected String nodeId;

    @JsonProperty("avatar_url")
    protected String avatarUrl;

    @JsonProperty("gravatar_id")
    protected String gravatarId;

    @JsonProperty("url")
    protected String url;

    @JsonProperty("html_url")
    protected String htmlUrl;

    @JsonProperty("followers_url")
    protected String followersUrl;

    @JsonProperty("following_url")
    protected String followingUrl;

    @JsonProperty("gists_url")
    protected String gistsUrl;

    @JsonProperty("starred_url")
    protected String starredUrl;

    @JsonProperty("subscriptions_url")
    protected String subscriptionsUrl;

    @JsonProperty("organizations_url")
    protected String organizationsUrl;

    @JsonProperty("repos_url")
    protected String reposUrl;

    @JsonProperty("events_url")
    protected String eventsUrl;

    @JsonProperty("received_events_url")
    protected String receivedEventsUrl;

    @JsonProperty("type")
    protected String type;

    @JsonProperty("site_admin")
    protected Boolean siteAdmin;

    @JsonProperty("name")
    protected String name;

    @JsonProperty("company")
    protected String company;

    @JsonProperty("blog")
    protected String blog;

    @JsonProperty("location")
    protected String location;

    @JsonProperty("email")
    protected String email;

    @JsonProperty("hireable")
    protected String hireable;

    @JsonProperty("bio;")
    protected String bio;

    @JsonProperty("twitter_username")
    protected String twitterUsername;

    @JsonProperty("public_repos")
    protected Integer publicRepos;

    @JsonProperty("public_gists")
    protected Integer publicGists;

    @JsonProperty("followers")
    protected Integer followers;

    @JsonProperty("following")
    protected Integer following;

    @JsonProperty("createdAt")
    protected String created_at;

    @JsonProperty("updatedAt")
    protected String updated_at;

    public GitHubIdPUser() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGravatarId() {
        return gravatarId;
    }

    public void setGravatarId(String gravatarId) {
        this.gravatarId = gravatarId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getFollowingUrl() {
        return followingUrl;
    }

    public void setFollowingUrl(String followingUrl) {
        this.followingUrl = followingUrl;
    }

    public String getGistsUrl() {
        return gistsUrl;
    }

    public void setGistsUrl(String gistsUrl) {
        this.gistsUrl = gistsUrl;
    }

    public String getStarredUrl() {
        return starredUrl;
    }

    public void setStarredUrl(String starredUrl) {
        this.starredUrl = starredUrl;
    }

    public String getSubscriptionsUrl() {
        return subscriptionsUrl;
    }

    public void setSubscriptionsUrl(String subscriptionsUrl) {
        this.subscriptionsUrl = subscriptionsUrl;
    }

    public String getOrganizationsUrl() {
        return organizationsUrl;
    }

    public void setOrganizationsUrl(String organizationsUrl) {
        this.organizationsUrl = organizationsUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getReceivedEventsUrl() {
        return receivedEventsUrl;
    }

    public void setReceivedEventsUrl(String receivedEventsUrl) {
        this.receivedEventsUrl = receivedEventsUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(Boolean siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHireable() {
        return hireable;
    }

    public void setHireable(String hireable) {
        this.hireable = hireable;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    public Integer getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(Integer publicRepos) {
        this.publicRepos = publicRepos;
    }

    public Integer getPublicGists() {
        return publicGists;
    }

    public void setPublicGists(Integer publicGists) {
        this.publicGists = publicGists;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "GitHubIdPUser{" +
            "login='" + login + '\'' +
            ", id=" + id +
            ", nodeId='" + nodeId + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", gravatarId='" + gravatarId + '\'' +
            ", url='" + url + '\'' +
            ", htmlUrl='" + htmlUrl + '\'' +
            ", followersUrl='" + followersUrl + '\'' +
            ", followingUrl='" + followingUrl + '\'' +
            ", gistsUrl='" + gistsUrl + '\'' +
            ", starredUrl='" + starredUrl + '\'' +
            ", subscriptionsUrl='" + subscriptionsUrl + '\'' +
            ", organizationsUrl='" + organizationsUrl + '\'' +
            ", reposUrl='" + reposUrl + '\'' +
            ", eventsUrl='" + eventsUrl + '\'' +
            ", receivedEventsUrl='" + receivedEventsUrl + '\'' +
            ", type='" + type + '\'' +
            ", siteAdmin=" + siteAdmin +
            ", name='" + name + '\'' +
            ", company='" + company + '\'' +
            ", blog='" + blog + '\'' +
            ", location='" + location + '\'' +
            ", email='" + email + '\'' +
            ", hireable='" + hireable + '\'' +
            ", bio='" + bio + '\'' +
            ", twitterUsername='" + twitterUsername + '\'' +
            ", publicRepos=" + publicRepos +
            ", publicGists=" + publicGists +
            ", followers=" + followers +
            ", following=" + following +
            ", created_at='" + created_at + '\'' +
            ", updated_at='" + updated_at + '\'' +
            '}';
    }
}
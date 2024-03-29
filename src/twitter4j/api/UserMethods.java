/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j.api;

import twitter4j.*;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface UserMethods {
    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter. The author's most recent status will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/show.json
     *
     * @param screenName the screen name of the user for whom to request the detail
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/show">GET users/show | Twitter Developers</a>
     */
    User showUser(String screenName) throws TwitterException;
    User showUserv1(String screenName) throws TwitterException;

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter. The author's most recent status will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/show.json
     *
     * @param userId the ID of the user for whom to request the detail
     * @return users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/show">GET users/show | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    User showUser(long userId) throws TwitterException;
    User showUserv1(long userId) throws TwitterException;

    /**
     * Return up to 100 users worth of extended information, specified by either ID, screen name, or combination of the two. The author's most recent status (if the authenticating user has permission) will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/lookup.json
     *
     * @param screenNames Specifies the screen names of the users to return.
     * @return users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/lookup">GET users/lookup | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> lookupUsers(String[] screenNames) throws TwitterException;

    /**
     * Return up to 100 users worth of extended information, specified by either ID, screen name, or combination of the two. The author's most recent status (if the authenticating user has permission) will be returned inline.
     * <br>This method calls http://api.twitter.com/1/users/lookup.json
     *
     * @param ids Specifies the screen names of the users to return.
     * @return users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/lookup">GET users/lookup | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> lookupUsers(long[] ids) throws TwitterException;

    /**
     * Run a search for users similar to the Find People button on Twitter.com; the same results returned by people search on Twitter.com will be returned by using this API.<br>
     * Usage note: It is only possible to retrieve the first 1000 matches from this API.
     * <br>This method calls http://api.twitter.com/1/users/search.json
     *
     * @param query The query to run against people search.
     * @param page  Specifies the page of results to retrieve. Number of statuses per page is fixed to 20.
     * @return the list of Users matches the provided
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/search">GET users/search | Twitter Developers</a>
     */
    ResponseList<User> searchUsers(String query, int page) throws TwitterException;

    /**
     * Access to Twitter's suggested user list. This returns the list of suggested user categories. The category can be used in the users/suggestions/category endpoint to get the users in that category.
     * <br>This method calls http://api.twitter.com/1/users/suggestions/:slug.json
     *
     * @return list of suggested user categories.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/suggestions/:slug">GET users/suggestions/:slug | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<Category> getSuggestedUserCategories() throws TwitterException;

    /**
     * Access the users in a given category of the Twitter suggested user list.<br>
     * It is recommended that end clients cache this data for no more than one hour.
     * <br>This method calls http://api.twitter.com/1/users/suggestions/:slug.json
     *
     * @param categorySlug slug
     * @return list of suggested users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/suggestions/slug">GET users/suggestions/slug | Twitter Developers</a>
     * @since Twitter4J 2.1.1
     */
    ResponseList<User> getUserSuggestions(String categorySlug) throws TwitterException;

    /**
     * Access the users in a given category of the Twitter suggested user list and return their most recent status if they are not a protected user.
     * <br>This method has not been finalized and the interface is subject to change in incompatible ways.
     * <br>This method calls http://api.twitter.com/1/users/suggestions/:slug/members.json
     *
     * @param categorySlug slug
     * @return list of suggested users
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.9
     */
    ResponseList<User> getMemberSuggestions(String categorySlug) throws TwitterException;

    /**
     * Access the profile image in various sizes for the user with the indicated screen_name. If no size is provided the normal image is returned. This resource does not return JSON or XML, but instead returns a 302 redirect to the actual image resource.
     * This method should only be used by application developers to lookup or check the profile image URL for a user. This method must not be used as the image source URL presented to users of your application.
     * <br>This method calls http://api.twitter.com/1/users/profile_image/:screen_name.json
     *
     * @param screenName The screen name of the user for whom to return results for.
     * @param size       Specifies the size of image to fetch. Not specifying a size will give the default, normal size of 48px by 48px. Valid options include: bigger - 73px by 73px normal - 48px by 48px mini - 24px by 24px
     * @return profile image
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/profile_image/:screen_name">GET users/profile_image/:screen_name | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    ProfileImage getProfileImage(String screenName, ProfileImage.ImageSize size) throws TwitterException;

    /**
     * Returns an array of users who can contribute to the specified account.
     *
     * @param screenName The screen name of the user for whom to return results for
     * @return list of contributors
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/contributors">GET users/contributors | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<User> getContributors(String screenName) throws TwitterException;

    /**
     * Returns an array of users who can contribute to the specified account.
     *
     * @param userId The user id of the user for whom to return results for
     * @return list of contributors
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/contributors">GET users/contributors | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<User> getContributors(long userId) throws TwitterException;

    /**
     * Returns an array of users that the specified user can contribute to.
     *
     * @param screenName The screen name of the user for whom to return results for
     * @return list of contributors
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/contributees">GET users/contributors | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<User> getContributees(String screenName) throws TwitterException;

    /**
     * Returns an array of users that the specified user can contribute to.
     *
     * @param userId The user id of the user for whom to return results for
     * @return list of contributors
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/docs/api/1/get/users/contributees">GET users/contributors | Twitter Developers</a>
     * @since Twitter4J 3.0.0
     */
    ResponseList<User> getContributees(long userId) throws TwitterException;
}

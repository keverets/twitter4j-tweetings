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

package twitter4j.media;

import twitter4j.TwitterException;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.logging.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * @author Rémy Rakic - remy.rakic at gmail.com
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @author withgod - noname at withgod.jp
 * @since Twitter4J 2.1.8
 */
abstract class AbstractImageUploadImpl implements ImageUpload {
    public static final String TWITTER_VERIFY_CREDENTIALS_JSON = "https://api.twitter.com/1/account/verify_credentials.json";
    public static final String TWITTER_VERIFY_CREDENTIALS_XML = "https://api.twitter.com/1/account/verify_credentials.xml";

    private HttpClientWrapper client;

    protected Configuration conf = null;
    protected String apiKey = null;
    protected OAuthAuthorization oauth = null;
    protected String uploadUrl = null;
    protected HttpParameter[] postParameter = null;
    protected HttpParameter[] appendParameter = null;
    protected HttpParameter image = null;
    protected HttpParameter message = null;
    protected Map<String, String> headers = new HashMap<String, String>();
    protected HttpResponse httpResponse = null;
    protected static final Logger logger = Logger.getLogger();

    AbstractImageUploadImpl(Configuration conf, OAuthAuthorization oauth) {
        this.oauth = oauth;
        this.conf = conf;
        client = new HttpClientWrapper(conf);
    }

    public AbstractImageUploadImpl(Configuration conf, String apiKey, OAuthAuthorization oauth) {
        this(conf, oauth);
        this.apiKey = apiKey;
    }

    @Override
    public String upload(String imageFileName, InputStream imageBody) throws TwitterException {
        this.image = new HttpParameter("media", imageFileName, imageBody);
        return upload();
    }

    @Override
    public String upload(String imageFileName, InputStream imageBody, String message) throws TwitterException {
        this.image = new HttpParameter("media", imageFileName, imageBody);
        this.message = new HttpParameter("message", message);
        return upload();
    }

    @Override
    public String upload(File file, String message) throws TwitterException {
        this.image = new HttpParameter("media", file);
        this.message = new HttpParameter("message", message);
        return upload();
    }

    @Override
    public String upload(File file) throws TwitterException {
        this.image = new HttpParameter("media", file);
        return upload();
    }

    private String upload() throws TwitterException {
        if (conf.getMediaProviderParameters() != null) {
            Set set = conf.getMediaProviderParameters().keySet();
            HttpParameter[] params = new HttpParameter[set.size()];
            int pos = 0;
            for (Object k : set) {
                String v = conf.getMediaProviderParameters().getProperty((String) k);
                params[pos] = new HttpParameter((String) k, v);
                pos++;
            }
            this.appendParameter = params;
        }
        preUpload();
        if (this.postParameter == null) {
            throw new AssertionError("Incomplete implementation. postParameter is not set.");
        }
        if (this.uploadUrl == null) {
            throw new AssertionError("Incomplete implementation. uploadUrl is not set.");
        }
        if (conf.getMediaProviderParameters() != null && this.appendParameter.length > 0) {
            this.postParameter = appendHttpParameters(this.postParameter, this.appendParameter);
        }

        httpResponse = client.post(uploadUrl, postParameter, headers);

        String mediaUrl = postUpload();
        logger.debug("uploaded url [" + mediaUrl + "]");

        return mediaUrl;
    }

    protected abstract void preUpload() throws TwitterException;

    protected abstract String postUpload() throws TwitterException;

    protected HttpParameter[] appendHttpParameters(HttpParameter[] src, HttpParameter[] dst) {
        int srcLen = src.length;
        int dstLen = dst.length;
        HttpParameter[] ret = new HttpParameter[srcLen + dstLen];
        System.arraycopy(src, 0, ret, 0, srcLen);
        System.arraycopy(dst, 0, ret, srcLen, dstLen);
        return ret;
    }

    protected String generateVerifyCredentialsAuthorizationHeader(String verifyCredentialsUrl) {
        List<HttpParameter> oauthSignatureParams = oauth.generateOAuthSignatureHttpParams("GET", verifyCredentialsUrl);
        return "OAuth realm=\"http://api.twitter.com/\"," + OAuthAuthorization.encodeParameters(oauthSignatureParams, ",", true);
    }

    protected String generateVerifyCredentialsAuthorizationURL(String verifyCredentialsUrl) {
        List<HttpParameter> oauthSignatureParams = oauth.generateOAuthSignatureHttpParams("GET", verifyCredentialsUrl);
        return verifyCredentialsUrl + "?" + OAuthAuthorization.encodeParameters(oauthSignatureParams);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractImageUploadImpl that = (AbstractImageUploadImpl) o;

        if (apiKey != null ? !apiKey.equals(that.apiKey) : that.apiKey != null) return false;
        if (!Arrays.equals(appendParameter, that.appendParameter)) return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        if (conf != null ? !conf.equals(that.conf) : that.conf != null) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (httpResponse != null ? !httpResponse.equals(that.httpResponse) : that.httpResponse != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (oauth != null ? !oauth.equals(that.oauth) : that.oauth != null) return false;
        if (!Arrays.equals(postParameter, that.postParameter)) return false;
        if (uploadUrl != null ? !uploadUrl.equals(that.uploadUrl) : that.uploadUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = client != null ? client.hashCode() : 0;
        result = 31 * result + (conf != null ? conf.hashCode() : 0);
        result = 31 * result + (apiKey != null ? apiKey.hashCode() : 0);
        result = 31 * result + (oauth != null ? oauth.hashCode() : 0);
        result = 31 * result + (uploadUrl != null ? uploadUrl.hashCode() : 0);
        result = 31 * result + (postParameter != null ? Arrays.hashCode(postParameter) : 0);
        result = 31 * result + (appendParameter != null ? Arrays.hashCode(appendParameter) : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (httpResponse != null ? httpResponse.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AbstractImageUploadImpl{" +
                "client=" + client +
                ", conf=" + conf +
                ", apiKey='" + apiKey + '\'' +
                ", oauth=" + oauth +
                ", uploadUrl='" + uploadUrl + '\'' +
                ", postParameter=" + (postParameter == null ? null : Arrays.asList(postParameter)) +
                ", appendParameter=" + (appendParameter == null ? null : Arrays.asList(appendParameter)) +
                ", image=" + image +
                ", message=" + message +
                ", headers=" + headers +
                ", httpResponse=" + httpResponse +
                '}';
    }
}

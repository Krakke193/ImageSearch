package com.kpi.imagesearch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sasha on 17/4/16.
 * This classed is used to construct query parameters.
 * Implementation of builder pattern to ensure easy-way expanding of query combinations.
 */
public class QueryManager {
    private static final String TAG = QueryManager.class.getSimpleName();
    private static final String BASE_SEARCH_URL = "https://www.googleapis.com/customsearch/v1?";

    private String mKey;
    private String mCx;
    private String mQuery;
    private String mSearchType;
    private String mAlt;
    private String mImgSize;

    public static class Builder {
        private final String mKey;
        private final String mCx;
        private final String mQuery;
        private String mSearchType;
        private String mAlt;
        private String mImgSize;

        public Builder(String apiKey, String cx, String query) {
            mKey = apiKey;
            mCx = cx;
            mQuery = query;
        }

        public Builder setSearchType(String searchType) {
            mSearchType = searchType;
            return this;
        }

        public Builder setResponseType(String responseType) {
            mAlt = responseType;
            return this;
        }

        public Builder setImageSize(ImageSizes imageSize) {
            switch (imageSize) {
                case SMALL:
                    mImgSize = "small";
                    break;
                case MEDIUM:
                    mImgSize = "medium";
                    break;
                case LARGE:
                    mImgSize = "large";
                    break;
            }
            return this;
        }

        public QueryManager build() {
            return new QueryManager(this);
        }

        public enum ImageSizes {
            SMALL, MEDIUM, LARGE
        }
    }

    public void setKey(String key) {
        mKey = key;
    }

    public void setCx(String cx) {
        mCx = cx;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public void setSearchType(String searchType) {
        mSearchType = searchType;
    }

    public void setAlt(String alt) {
        mAlt = alt;
    }

    public void setImgSize(String imgSize) {
        mImgSize = imgSize;
    }

    private QueryManager(Builder builder) {
        setKey(builder.mKey);
        setCx(builder.mCx);
        setSearchType(builder.mSearchType);
        setAlt(builder.mAlt);
        setQuery(builder.mQuery);
        setImgSize(builder.mImgSize);
    }

    public URL getQueryUrl() throws MalformedURLException{
        Map<String, String> params = new HashMap<>();
        params.put("key", mKey);
        params.put("cx", mCx);
        params.put("alt", mAlt == null ? "json" : mAlt);
        params.put("searchType", mSearchType);
        params.put("q", mQuery);
        params.put("imgSize", mImgSize == null ? "medium" : mImgSize);

        return new URL(constructQuery(params));
    }

    private String constructQuery(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_SEARCH_URL);

        for (String key : params.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(params.get(key));
            stringBuilder.append("&");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}

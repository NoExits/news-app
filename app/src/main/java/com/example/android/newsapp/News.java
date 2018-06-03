package com.example.android.newsapp;

import java.util.Date;

public class News {

    /**
     * Title of the news
     */
    private final String mTitle;

    /**
     * Section name of the news
     */
    private final String mSectionName;

    /**
     * Author name of the news
     */
    private final String mAuthorName;

    /**
     * Publication date of the news
     */
    private final Date mPublicationDate;

    /**
     * Website URL of the news
     */
    private final String mUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title           is the title of the news
     * @param section         is the section where the news happened
     * @param authorName  is the news author full name
     * @param publicationDate is the news publication date
     * @param url             is the website URL to find more details about the news
     */
    public News(String title, String section, String authorName, Date publicationDate, String url) {
        mTitle = title;
        mSectionName = section;
        mAuthorName = authorName;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    /**
     * Returns the magnitude of the news.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the section name of the news.
     */
    public String getSectionName() {
        return mSectionName;
    }

    /**
     * Returns the author of the news.
     */
    public String getAuthorName() {
        return mAuthorName;
    }

    /**
     * Returns the publication date of the news.
     */
    public Date getPublicationDate() {
        return mPublicationDate;
    }

    /**
     * Returns the website URL to find more information about the news.
     */
    public String getUrl() {
        return mUrl;
    }
}
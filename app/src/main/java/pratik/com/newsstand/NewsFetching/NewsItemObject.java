package pratik.com.newsstand.NewsFetching;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Pratz on 24-01-2018.
 */

public class NewsItemObject {

    private String id;
    private String source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String imgurl;
    private String articleDate;
    private Bitmap articleImage;
    private Drawable articleSourceLogo;
    private String articleSourceID;
    private int optionsColor;
    private boolean isBookmarked;
    private int sequenceNo;

    private String articleContent;

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Bitmap getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(Bitmap articleImage) {
        this.articleImage = articleImage;
    }

    public Drawable getArticleSourceLogo() {
        return articleSourceLogo;
    }

    public void setArticleSourceLogo(Drawable articleSourceLogo) {
        this.articleSourceLogo = articleSourceLogo;
    }

    public String getArticleSourceID() {
        return articleSourceID;
    }

    public void setArticleSourceID(String articleSourceID) {
        this.articleSourceID = articleSourceID;
    }

    public int getOptionsColor() {
        return optionsColor;
    }

    public void setOptionsColor(int optionsColor) {
        this.optionsColor = optionsColor;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
}

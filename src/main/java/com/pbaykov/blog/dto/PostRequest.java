package com.pbaykov.blog.dto;

public class PostRequest {
    private String title;
    private String content;
    private Long category;

    private String image;

    public PostRequest() {}

    public PostRequest(String title, String content, Long category, String image) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

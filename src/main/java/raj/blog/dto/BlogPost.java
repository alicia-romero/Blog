/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raj.blog.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 *
 * @author romeroalicia
 */
public class BlogPost {
    
    private int blogPostId;
    private LocalDateTime releaseDate;
    
    @Future(message = "Expiry Date must be in the future.")
    @NotNull(message = "Expiry Date must not be empty.")
    private LocalDateTime expiryDate;
    
    @NotBlank(message = "Content must not be empty.")
    private String content;
    
    private List<Hashtag> hashtags;

    public int getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(int blogPostId) {
        this.blogPostId = blogPostId;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public String toString() {
        return "BlogPost{" + "blogPostId=" + blogPostId + ", releaseDate=" + releaseDate + ", expiryDate=" + expiryDate + ", content=" + content + ", hashtags=" + hashtags + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.blogPostId;
        hash = 97 * hash + Objects.hashCode(this.releaseDate);
        hash = 97 * hash + Objects.hashCode(this.expiryDate);
        hash = 97 * hash + Objects.hashCode(this.content);
        hash = 97 * hash + Objects.hashCode(this.hashtags);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlogPost other = (BlogPost) obj;
        if (this.blogPostId != other.blogPostId) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.releaseDate, other.releaseDate)) {
            return false;
        }
        if (!Objects.equals(this.expiryDate, other.expiryDate)) {
            return false;
        }
        return Objects.equals(this.hashtags, other.hashtags);
    }
}

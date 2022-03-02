/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raj.blog.dto;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author romeroalicia
 */
public class Hashtag {
    
    private int hashtagId;
    private String hashtagValue;
    private int trendingNumber; // the number of hashtags present on the site

    public int getHashtagId() {
        return hashtagId;
    }

    public void setHashtagId(int hashtagId) {
        this.hashtagId = hashtagId;
    }

    public String getHashtagValue() {
        return hashtagValue;
    }

    public void setHashtagValue(String hashtagValue) {
        this.hashtagValue = hashtagValue;
    }

    public int getTrendingNumber() {
        return trendingNumber;
    }

    public void setTrendingNumber(int trendingNumber) {
        this.trendingNumber = trendingNumber;
    }

    @Override
    public String toString() {
        return "Hashtag{" + "hashtagId=" + hashtagId + ", hashtagValue=" + hashtagValue + ", trendingNumber=" + trendingNumber + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.hashtagId;
        hash = 59 * hash + Objects.hashCode(this.hashtagValue);
        hash = 59 * hash + this.trendingNumber;
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
        final Hashtag other = (Hashtag) obj;
        if (this.hashtagId != other.hashtagId) {
            return false;
        }
        if (this.trendingNumber != other.trendingNumber) {
            return false;
        }
        return Objects.equals(this.hashtagValue, other.hashtagValue);
    }
}

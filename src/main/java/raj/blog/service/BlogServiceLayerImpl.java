/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raj.blog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import raj.blog.dao.BlogPostDao;
import raj.blog.dao.HashtagDao;
import raj.blog.dto.Hashtag;

/**
 *
 * @author romeroalicia
 */
@Repository
public class BlogServiceLayerImpl implements BlogServiceLayer {
    
    @Autowired
    BlogPostDao blogPostDao;
    
    @Autowired
    HashtagDao hashtagDao;
    
    /*
    * oldHashtags - an empty list incase of a new post
    * - contains the old oldHashtags of the post, incase of editing
    */
    @Override
    public List<Hashtag> getHashtagsFromContent(String content, List<Hashtag> oldHashtags) {
        String[] contentStr = content.split("\\$| |<[^>]*>"); // split content by spaces and html tags
            
        Map<String, Hashtag> allHashtagsMappedToValue = new HashMap<>();
        Map<String, Hashtag> oldHashtagsMappedToValue = new HashMap<>();
            
            
        for (Hashtag hashtag: hashtagDao.getAllHashtags()) {
            allHashtagsMappedToValue.put(hashtag.getHashtagValue(), hashtag);
            if (oldHashtags.contains(hashtag)) {
                oldHashtagsMappedToValue.put(hashtag.getHashtagValue(), hashtag);
            }  
        }
            
        List<Hashtag> newHashtags = new ArrayList<>();
            
            
        for (String str: contentStr) {
            str = str
                    .replaceAll("[\\r\\n]", "") // remove new lines
                    .replaceAll("[^\\w]", " ") // replace all non-word chars with a space
                    .replaceFirst(" ", "#") // replace first space with #
                    .trim(); // trim the excess spaces

            if (str.startsWith("#")) {
                Hashtag hashtag = new Hashtag();
                hashtag.setHashtagValue(str);
                    
                if (!oldHashtagsMappedToValue.containsKey(str) && allHashtagsMappedToValue.containsKey(str)) {
                    // add one to trendingNumber, if hastag is not in oldHashtags but it already exists
                    hashtag.setHashtagId(allHashtagsMappedToValue.get(str).getHashtagId());
                    hashtag.setTrendingNumber(allHashtagsMappedToValue.get(str).getTrendingNumber() + 1);
                    hashtagDao.updateHashtag(hashtag);
                } else if (!oldHashtagsMappedToValue.containsKey(str) && !allHashtagsMappedToValue.containsKey(str)) {
                    // create hashtag, if it does not yet exists
                    hashtag.setTrendingNumber(1);
                    hashtag = hashtagDao.addHashtag(hashtag);
                } else if (oldHashtagsMappedToValue.containsKey(str) && allHashtagsMappedToValue.containsKey(str)) {
                    // if hashtag is in old oldHashtags, just add it to newHashtags
                    hashtag.setHashtagId(allHashtagsMappedToValue.get(str).getHashtagId());
                    hashtag.setTrendingNumber(allHashtagsMappedToValue.get(str).getTrendingNumber());
                    hashtagDao.updateHashtag(hashtag);
                }
                newHashtags.add(hashtag);
            }
        }
            
        for (Hashtag hashtag: oldHashtags) {
            if (!newHashtags.contains(hashtag) && hashtag.getTrendingNumber() == 1) {
                // if hashtag is in oldHshtags but not in newHashtags, and it is the only one
                // in posts so far, than delete it from databse
                hashtagDao.deleteHashtagById(hashtag.getHashtagId());
            } else if (!newHashtags.contains(hashtag)) {
                // if it is not the only one we just need to subtract one from the trendingNumber 
                hashtag.setTrendingNumber(hashtag.getTrendingNumber() - 1);
                hashtagDao.updateHashtag(hashtag);
            }
        }
            
        return newHashtags;
    }
    
}

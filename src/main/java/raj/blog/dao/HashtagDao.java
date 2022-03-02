/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package raj.blog.dao;

import java.util.List;
import raj.blog.dto.BlogPost;
import raj.blog.dto.Hashtag;

/**
 *
 * @author romeroalicia
 */
public interface HashtagDao {
    Hashtag getHashtagById(int id);
    List<Hashtag> getAllHashtags();
    List<Hashtag> getCurrentHashtags();
    Hashtag addHashtag(Hashtag hashtag);
    void updateHashtag(Hashtag hashtag);
    void deleteHashtagById(int id);
}

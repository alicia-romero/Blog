/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package raj.blog.service;

import java.util.List;
import raj.blog.dto.Hashtag;

/**
 *
 * @author romeroalicia
 */
public interface BlogServiceLayer {
    
    List<Hashtag> getHashtagsFromContent(String content, List<Hashtag> hashtags);
}

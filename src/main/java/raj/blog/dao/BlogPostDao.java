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
public interface BlogPostDao {
    BlogPost getBlogPostById(int id);
    List<BlogPost> getAllBlogPosts();
    List<BlogPost> getCurrentBlogPosts();
    BlogPost addBlogPost(BlogPost blogPost);
    void updateBlogPost(BlogPost blogPost);
    void deleteBlogPostById(int id);
    
    List<BlogPost> getBlogPostsForHashtag(Hashtag hashtag);
}

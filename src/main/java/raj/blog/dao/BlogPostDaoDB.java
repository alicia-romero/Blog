/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raj.blog.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import raj.blog.dao.HashtagDaoDB.HashtagMapper;
import raj.blog.dto.BlogPost;
import raj.blog.dto.Hashtag;

/**
 *
 * @author romeroalicia
 */
@Repository
public class BlogPostDaoDB implements BlogPostDao {
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public BlogPost getBlogPostById(int id) {
        try {
            final String SELECT_BLOGPOST_BY_ID = "SELECT * FROM blogPost WHERE blogPostId = ?;";
            BlogPost blogPost = jdbc.queryForObject(SELECT_BLOGPOST_BY_ID, new BlogPostMapper(), id);
            blogPost.setHashtags(getHashtagsForBlogPost(id));
            return blogPost;
        } catch(DataAccessException ex) {
            return null;
        }
    }
    
    private List<Hashtag> getHashtagsForBlogPost(int id) {
        final String SELECT_HASHTAGS_FOR_BLOGPOST = "SELECT h.* FROM hashtag h "
                + "JOIN blogPostHashtag bh ON h.hashtagId = bh.hashtagId WHERE bh.blogPostId = ?;";
        return jdbc.query(SELECT_HASHTAGS_FOR_BLOGPOST, new HashtagMapper(), id);
    }

    @Override
    public List<BlogPost> getAllBlogPosts() {
        final String SELECT_ALL_BLOGPOSTS = "SELECT * FROM blogPost ORDER BY releaseDate DESC;";
        List<BlogPost> blogPosts = jdbc.query(SELECT_ALL_BLOGPOSTS, new BlogPostMapper());
        associateHashtags(blogPosts);
        return blogPosts;
    }
    
     private void associateHashtags(List<BlogPost> blogPosts) {
        for (BlogPost blogPost: blogPosts) {
            blogPost.setHashtags(getHashtagsForBlogPost(blogPost.getBlogPostId()));
        }
        
    }

    @Override
    public List<BlogPost> getCurrentBlogPosts() {
        final String SELECT_CURRENT_BLOGPOSTS = "SELECT * FROM blogPost "
                + "WHERE expiryDate > ? "
                + "ORDER BY releaseDate DESC;";
        LocalDateTime now = LocalDateTime.now();
        List<BlogPost> blogPosts = jdbc.query(SELECT_CURRENT_BLOGPOSTS, new BlogPostMapper(), now);
        associateHashtags(blogPosts);
        return blogPosts;
    }

    @Override
    @Transactional
    public BlogPost addBlogPost(BlogPost blogPost) {
        final String INSERT_BLOGPOST = "INSERT INTO blogPost(releaseDate, expiryDate, content) "
                + "VALUES(?,?,?);";
        jdbc.update(INSERT_BLOGPOST,
                blogPost.getReleaseDate(),
                blogPost.getExpiryDate(),
                blogPost.getContent());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        blogPost.setBlogPostId(newId);
        insertBlogPostHashtag(blogPost);
        return blogPost;
    }
    
    private void insertBlogPostHashtag(BlogPost blogPost) {
        final String INSERT_BLOGPOST_HASHTAG = "INSERT INTO "
                + "blogPostHashtag(blogPostId, hashtagId) VALUES(?,?);";
        for(Hashtag hashtag : blogPost.getHashtags()) {
            jdbc.update(INSERT_BLOGPOST_HASHTAG, 
                    blogPost.getBlogPostId(),
                    hashtag.getHashtagId());
        }
    }

    @Override
    @Transactional
    public void updateBlogPost(BlogPost blogPost) {
        final String UPDATE_BLOGPOST = "UPDATE blogPost SET releaseDate = ?, expiryDate = ?, content = ? "
                + "WHERE blogPostId = ?;";
      
        jdbc.update(UPDATE_BLOGPOST, 
                blogPost.getReleaseDate(),
                blogPost.getExpiryDate(),
                blogPost.getContent(),
                blogPost.getBlogPostId());
        
        final String DELETE_BLOGPOST_HASHTAG = "DELETE FROM blogPostHashtag WHERE blogPostId = ?;";
        jdbc.update(DELETE_BLOGPOST_HASHTAG, blogPost.getBlogPostId());
        insertBlogPostHashtag(blogPost);
    }

    @Override
    public void deleteBlogPostById(int id) {
        final String DELETE_BLOGPOST_HASHTAG = "DELETE FROM blogPostHashtag WHERE blogPostId = ?;";
        jdbc.update(DELETE_BLOGPOST_HASHTAG, id);
        
        final String DELETE_BLOGPOST = "DELETE FROM blogPost WHERE blogPostId = ?;";
        jdbc.update(DELETE_BLOGPOST, id);
    }

    @Override
    public List<BlogPost> getBlogPostsForHashtag(Hashtag hashtag) {
        final String SELECT_BLOGPOSTS_FOR_HASHTAG = "SELECT b.* FROM blogPost b "
                + "JOIN blogPostHashtag bh ON b.blogPostId = bh.blogPostId "
                + "WHERE bh.hashtagId = ? AND b.expiryDate > ? "
                + "ORDER BY b.releaseDate DESC;";
        LocalDateTime now = LocalDateTime.now();
        List<BlogPost> blogPosts = jdbc.query(SELECT_BLOGPOSTS_FOR_HASHTAG, 
                new BlogPostMapper(), hashtag.getHashtagId(), now);
        associateHashtags(blogPosts);
        return blogPosts;
    }
    
    public static final class BlogPostMapper implements RowMapper<BlogPost> {

        @Override
        public BlogPost mapRow(ResultSet rs, int rowNum) throws SQLException {
            BlogPost blogPost = new BlogPost();
            blogPost.setBlogPostId(rs.getInt("blogPostId"));
            blogPost.setReleaseDate(rs.getTimestamp("releaseDate").toLocalDateTime());
            blogPost.setExpiryDate(rs.getTimestamp("expiryDate").toLocalDateTime());
            blogPost.setContent(rs.getString("content"));
            
            return blogPost;
        }
        
    } 
}

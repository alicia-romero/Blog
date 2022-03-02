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
import raj.blog.dto.Hashtag;

/**
 *
 * @author romeroalicia
 */
@Repository
public class HashtagDaoDB implements HashtagDao {
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Hashtag getHashtagById(int id) {
        try {
            final String GET_HASHTAG_BY_ID = "SELECT * FROM hashtag WHERE hashtagId = ?;";
            return jdbc.queryForObject(GET_HASHTAG_BY_ID, new HashtagMapper(), id);
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Hashtag> getAllHashtags() {
        final String GET_ALL_HASHTAGS = "SELECT * FROM hashtag;";
        return jdbc.query(GET_ALL_HASHTAGS, new HashtagMapper());
    }
    
     @Override
    public List<Hashtag> getCurrentHashtags() {
        final String GET_ALL_HASHTAGS = "SELECT DISTINCT h.* FROM hashtag h "
                + "JOIN blogPostHashtag bh ON h.hashtagId = bh.hashtagId "
                + "JOIN blogPost b ON bh.blogPostId = b.blogPostId "
                + "WHERE b.expiryDate > ?;";
        LocalDateTime now = LocalDateTime.now();
        return jdbc.query(GET_ALL_HASHTAGS, new HashtagMapper(), now);
    }

    @Override
    @Transactional
    public Hashtag addHashtag(Hashtag hashtag) {
        final String INSERT_HASHTAG = "INSERT INTO hashtag(hashtagValue, trendingNumber) "
                + "VALUES(?,?);";
        jdbc.update(INSERT_HASHTAG, 
                hashtag.getHashtagValue(),
                hashtag.getTrendingNumber());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        hashtag.setHashtagId(newId);
        return hashtag;
    }

    @Override
    public void updateHashtag(Hashtag hashtag) {
        final String UPDATE_HASHTAG = "UPDATE hashtag SET trendingNumber = ? "
                + "WHERE hashtagId = ?;";
        jdbc.update(UPDATE_HASHTAG,
                hashtag.getTrendingNumber(),
                hashtag.getHashtagId());
    }

    @Override
    @Transactional
    public void deleteHashtagById(int id) {
        final String DELETE_BLOGPOST_HASHTAG = "DELETE FROM blogPostHashtag WHERE hashtagId = ?;";
        jdbc.update(DELETE_BLOGPOST_HASHTAG, id);
        
        final String DELETE_HASHTAG = "DELETE FROM hashtag WHERE hashtagId = ?;";
        jdbc.update(DELETE_HASHTAG, id);
    }
    
    public static final class HashtagMapper implements RowMapper<Hashtag> {

        @Override
        public Hashtag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hashtag hashtag = new Hashtag();
            hashtag.setHashtagId(rs.getInt("hashtagId"));
            hashtag.setHashtagValue(rs.getString("hashtagValue"));
            hashtag.setTrendingNumber(rs.getInt("trendingNumber"));
            
            return hashtag;
        }
        
    }
    
}

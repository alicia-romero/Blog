/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raj.blog.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import raj.blog.dao.BlogPostDao;
import raj.blog.dao.HashtagDao;
import raj.blog.dto.BlogPost;
import raj.blog.dto.Hashtag;
import raj.blog.service.BlogServiceLayer;

/**
 *
 * @author romeroalicia
 */
@Controller
public class BlogPostController {
    
    Set<ConstraintViolation<BlogPost>> violations = new HashSet<>();
    
    @Autowired
    BlogPostDao blogPostDao;
    
    @Autowired
    HashtagDao hashtagDao;
    
    @Autowired
    BlogServiceLayer service;
    
    @GetMapping("/admin/createBlogPost")
    public String displayCreateBlogPost(Model model) {
        model.addAttribute("errors", violations);
        return "/admin/createBlogPost";
    }
    
    @GetMapping("/admin/viewBlogPosts")
    public String displayViewBlogPosts(Model model) {
        List<BlogPost> blogPosts = blogPostDao.getCurrentBlogPosts();
        List<Hashtag> hashtags = hashtagDao.getCurrentHashtags();
        
        model.addAttribute("blogPosts", blogPosts);
        model.addAttribute("hashtags",hashtags);
        return "/admin/viewBlogPosts";
    }
    
    @PostMapping("/admin/saveBlogPost")
	public String saveBlogPost(HttpServletRequest request) {
            String content = request.getParameter("content");
            LocalDateTime releaseDate = LocalDateTime.now();
            LocalDateTime expiryDate;
            
            try {
                expiryDate = LocalDateTime.parse(request.getParameter("expiryDate"));
            } catch (Exception ex) {
                expiryDate = null;
            }
            
            BlogPost blogPost = new BlogPost();
            blogPost.setReleaseDate(releaseDate);
            blogPost.setExpiryDate(expiryDate);
            blogPost.setContent(content);
            
            List<Hashtag> hashtags = new ArrayList<>();
            
            hashtags = service.getHashtagsFromContent(content, hashtags);
            
            blogPost.setHashtags(hashtags);
            
            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            violations = validate.validate(blogPost);

            if(violations.isEmpty()) {
                blogPostDao.addBlogPost(blogPost);
            }
             
            return "redirect:/admin/createBlogPost";
	}

    @GetMapping("/admin/deleteBlogPost")
    public String deleteBlogPost(Integer id) {
        blogPostDao.deleteBlogPostById(id);
        return "redirect:/admin/viewBlogPosts";
    }
    
    @GetMapping("/admin/editBlogPost")
    public String editBlogPost(Integer id, Model model) {
        BlogPost blogPost = blogPostDao.getBlogPostById(id);
        List<Hashtag> hashtags = hashtagDao.getAllHashtags();
        
        model.addAttribute("blogPost", blogPost);
        model.addAttribute("hashtags", hashtags);
        return "/admin/editBlogPost";
    }
    
    @PostMapping("/admin/editBlogPost")
    public String performEditBlogPost(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        BlogPost blogPost = blogPostDao.getBlogPostById(id);
        
        LocalDateTime expiryDate = LocalDateTime.parse(request.getParameter("expiryDate"));
        String content = request.getParameter("content");
        
        blogPost.setExpiryDate(expiryDate);
        blogPost.setContent(content);

        List<Hashtag> hashtags = blogPost.getHashtags();
        
        hashtags = service.getHashtagsFromContent(content, hashtags);
            
        blogPost.setHashtags(hashtags);
        
        blogPostDao.updateBlogPost(blogPost);
        
        return "redirect:/admin/viewBlogPosts";
    }
    
    @GetMapping("/admin/viewBlogPostsByHashtag")
    public String viewBlogPostsByHashtag(Integer id, Model model) {
        Hashtag hashtag = hashtagDao.getHashtagById(id);
        List<BlogPost> blogPosts = blogPostDao.getBlogPostsForHashtag(hashtag);
        List<Hashtag> hashtags = hashtagDao.getCurrentHashtags();
        
        model.addAttribute("blogPosts", blogPosts);
        model.addAttribute("hashtags", hashtags);
        return "/admin/viewBlogPostsByHashtag";
    }
    
    @GetMapping("viewBlogPosts")
    public String displayViewBlogPostsVisitor(Model model) {
        List<BlogPost> blogPosts = blogPostDao.getCurrentBlogPosts();
        List<Hashtag> hashtags = hashtagDao.getCurrentHashtags();
        
        model.addAttribute("blogPosts", blogPosts);
        model.addAttribute("hashtags",hashtags);
        return "viewBlogPosts";
    }
    
    @GetMapping("viewBlogPostsByHashtag")
    public String viewBlogPostsByHashtagVisitor(Integer id, Model model) {
        Hashtag hashtag = hashtagDao.getHashtagById(id);
        List<BlogPost> blogPosts = blogPostDao.getBlogPostsForHashtag(hashtag);
        List<Hashtag> hashtags = hashtagDao.getCurrentHashtags();
        
        model.addAttribute("blogPosts", blogPosts);
        model.addAttribute("hashtags", hashtags);
        return "viewBlogPostsByHashtag";
    }

}

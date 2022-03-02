DROP DATABASE IF EXISTS blogPosts; 
CREATE DATABASE blogPosts;

USE blogPosts;

CREATE TABLE blogPost(
	blogPostId INT PRIMARY KEY AUTO_INCREMENT,
	releaseDate DATETIME NOT NULL,
    expiryDate DATETIME NOT NULL,
    content MEDIUMTEXT NOT NULL
);

CREATE TABLE hashtag(
	hashtagId INT PRIMARY KEY AUTO_INCREMENT,
    hashtagValue VARCHAR(20) NOT NULL UNIQUE,
	trendingNumber INT NOT NULL
);

CREATE TABLE blogPostHashtag(
	blogPostId INT NOT NULL,
	hashtagId INT NOT NULL,
    PRIMARY KEY(blogPostId, hashtagId),
	FOREIGN KEY (blogPostId) REFERENCES blogPost(blogPostId),
	FOREIGN KEY (hashtagId) REFERENCES hashtag(hashtagId)
);

INSERT INTO blogPost(releaseDate, expiryDate, content)
VALUES ("2022-02-26 17:08:56", "2022-03-05 17:08:00", "<p>Test Post 1</p> <p>This is a #test #post.</p>"),
		("2022-02-26 17:09:08", "2022-03-06 17:09:00", "<p>Test Post 2</p> <p>This is a #test #post.</p>"),
		("2022-02-26 17:09:36", "2022-03-05 17:09:00", "<p>Test Post 3</p> <p>This is a #small #post.</p>");

INSERT INTO hashtag(hashtagValue, trendingNumber)
VALUES ("#test", 2),
	    ("#post", 3),
	    ("#small", 1);
        
INSERT INTO blogPostHashtag(blogPostId, hashtagId) 
VALUES (1,1),
		(2,1),
        (1,2),
        (2,2),
        (3,2),
        (3,3);


	
# member select all
SELECT *
FROM V_member_all WHERE id = 1;

SELECT *
FROM member WHERE id = 1;

SELECT * 
FROM board_comment WHERE board_id = 23
ORDER BY group_id ASC,
id ASC;

SELECT * 
FROM board WHERE TYPE = 4 
ORDER BY id DESC;

SELECT 
bb.id,
bb.`type`,
bb.hit,
bb.subject,
bb.content,
bb.reg_date,
bb.edit_date,
bb.foret_id,
bb.writer_id,
cc.comment_count
FROM board as bb
LEFT JOIN (
SELECT *, COUNT(*) AS comment_count
FROM board_comment
GROUP BY board_id) as cc
ON bb.id = cc.board_id
WHERE TYPE = 4
ORDER BY cc.comment_count DESC,
bb.id DESC;

SELECT COUNT(*) count FROM foret_members 
WHERE forets_id = 24;



SELECT *
FROM board_comment;



INSERT INTO member(NAME, email, PASSWORD, nickname, birth, device_token)
VALUES ('11', '2222@email.com', '11', '11', '1944-12-22', '11');

(
SELECT *
FROM board WHERE foret_id = 8 AND TYPE = 1
ORDER BY id DESC
LIMIT 5
)
UNION ALL
(
SELECT *
FROM board WHERE foret_id = 8 AND TYPE = 3
ORDER BY id DESC
LIMIT 5
);










SELECT *
FROM V_foret_all;






SELECT *
FROM V_foret_rank;
SELECT *
FROM V_tag_rank;

SELECT * FROM
foret_photo;
SELECT *
FROM board;
SELECT *
FROM member_foret;
SELECT *
FROM foret;
SELECT *
FROM tag;
SELECT *
FROM foret;

SELECT COUNT(*).cnt
FROM boards
GROUP BY board.foret_id;

SELECT *
FROM V_foret_all
LEFT JOIN 
(SELECT board.foret_id, COUNT(*) AS cnt
FROM board
WHERE foret_id IS NOT NULL
GROUP BY foret_id
ORDER BY cnt DESC) temp
ON V_foret_all.id = temp.foret_id;

SELECT board.foret_id, COUNT(*) AS cnt
FROM board
WHERE foret_id IS NOT NULL
GROUP BY foret_id
ORDER BY cnt DESC;


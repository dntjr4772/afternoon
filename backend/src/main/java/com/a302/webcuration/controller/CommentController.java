package com.a302.webcuration.controller;

import com.a302.webcuration.common.BaseMessage;
import com.a302.webcuration.common.BaseStatus;
import com.a302.webcuration.domain.Comment.CommentDto;
import com.a302.webcuration.service.CommentService;
import com.a302.webcuration.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final JwtService jwtService;
    private final CommentService commentService;
    public static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @PostMapping("/{postsid}")
    public ResponseEntity createComment(@PathVariable Long postsid, @RequestBody  @Valid CommentDto.CreateCommentRequest request, @RequestHeader(value = "Authorization") String token)
    {
        Long accountId = jwtService.getAccountId(token);
        BaseMessage bm = commentService.createComment(postsid,accountId,request);

        return new ResponseEntity(new BaseMessage(bm.getStatus(),bm.getInfo()),HttpStatus.OK);

    }

}
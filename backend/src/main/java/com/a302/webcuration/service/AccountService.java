package com.a302.webcuration.service;

import com.a302.webcuration.common.BaseMessage;
import com.a302.webcuration.domain.Account.Account;
import com.a302.webcuration.domain.Account.AccountDto;
import com.a302.webcuration.domain.Account.AccountRepository;
import com.a302.webcuration.domain.Post.Posts;
import com.a302.webcuration.domain.Post.PostsDto;
import com.a302.webcuration.domain.Tag.Tag;
import com.a302.webcuration.domain.Tag.TagDto;
import com.a302.webcuration.domain.Tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    public static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;

    private final JwtService jwtService;

    //내정보
    public BaseMessage findAccountById(String token)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Long myId = jwtService.getAccountId(token);
            Account account = accountRepository.findAccountByAccountId(myId);
            logger.info("account = " + account.getAccountName());
            return profileMapping(account);
            /////////

            //좋아요한 게시물
        }catch (Exception e)
        {
            resultMap.put("errors",e);
            return new BaseMessage(HttpStatus.BAD_REQUEST,resultMap);
        }
    }

    BaseMessage profileMapping(Account account) throws Exception{
        try {
            AccountDto.AccountProfile profile = modelMapper.map(account, AccountDto.AccountProfile.class);

            //팔로잉, 팔로워
            List<AccountDto.FollowingDto> following = new ArrayList<>();
            List<AccountDto.FollowerDto> follower = new ArrayList<>();
            int followingCnt = account.getFollowing().size();
            int followerCnt = account.getFollower().size();
            Iterator<Account> iterFollower = account.getFollower().iterator();
            while (iterFollower.hasNext()) {
                follower.add(modelMapper.map(iterFollower.next(), AccountDto.FollowerDto.class));
            }
            Iterator<Account> iterFollowing = account.getFollowing().iterator();
            while (iterFollowing.hasNext()) {
                following.add(modelMapper.map(iterFollowing.next(), AccountDto.FollowingDto.class));
            }
            profile.setFollower(follower);
            profile.setFollowing(following);
            profile.setAccountFollowerCnt(followerCnt);
            profile.setAccountFollowingCnt(followingCnt);
            logger.info("account = " + account);
            //내가 쓴 게시물, 게시물 수
            List<PostsDto.PostsWithOnePhoto> writtenPosts=new ArrayList<>();
            int writtenPostsCnt=account.getMyPosts().size();
            for (int idx=0;idx<writtenPostsCnt;idx++){
                Posts posts=account.getMyPosts().get(idx);
                PostsDto.PostsWithOnePhoto writtenPost= PostsDto.PostsWithOnePhoto.builder()
                        .postsId(posts.getPostsId())
                        .postsWriter(posts.getPostWriter().getAccountName())
                        .postsTitle(posts.getPostsTitle())
                        .postsPhoto(posts.getPostsPhotos().get(0))
                        .build();
                writtenPosts.add(writtenPost);
            }
            profile.setWrittenPosts(writtenPosts);
            profile.setWrittenPostsCnt(writtenPostsCnt);

            return new BaseMessage(HttpStatus.OK, profile);
        }catch (Exception e){
            logger.error("e.getMessage() "+ e.getMessage());
            throw e;
        }
    }


    public void updateAccount(Long id,AccountDto.UpdateRequest request) {
        try {
            Account account = accountRepository.findAccountByAccountId(id);
            account.updateAccount(request);
        }catch (Exception e)
        {
            //TODO exception 구체화하기
            logger.error(e.getMessage());
        }
    }


    public BaseMessage follow(Long myId, Long yourId){
        Map<String, Object> resultMap = new HashMap<>();
        if(myId==yourId)
        {
            resultMap.put("message","자기 자신을 팔로우 할 수 없습니다.");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
        try {
            Account aAccount= accountRepository.findAccountByAccountId(myId);
            Account bAccount= accountRepository.findAccountByAccountId(yourId);
            aAccount.followAccount(bAccount);
            resultMap.put("message",myId+"가 "+yourId+" 를 팔로우하였습니다.");
            return new BaseMessage(HttpStatus.OK,resultMap);
        }catch (Exception e)
        {
            resultMap.put("message","객체가 존재하지 않습니다.");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
    }


    public void selectTag(AccountDto.AccountTagRequest accountTagRequest, String token){
        Long myId = jwtService.getAccountId(token);
        Account account=accountRepository.findAccountByAccountId(myId);

        try {
            for(TagDto.Tag tagDto: accountTagRequest.getTags()) {
                Tag tag= tagRepository.findTagByTagId(tagDto.getTagId());
                account.tagging(tag);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    // TODO: 2021-02-08 관심태그 취소 

}

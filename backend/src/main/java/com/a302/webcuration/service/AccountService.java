package com.a302.webcuration.service;

import com.a302.webcuration.common.BaseMessage;
import com.a302.webcuration.domain.Account.Account;
import com.a302.webcuration.domain.Account.AccountDto;
import com.a302.webcuration.domain.Account.AccountRepository;
import com.a302.webcuration.domain.Post.Posts;
import com.a302.webcuration.domain.Post.PostsDto;
import com.a302.webcuration.domain.Post.PostsRepository;
import com.a302.webcuration.domain.Tag.Tag;
import com.a302.webcuration.domain.Tag.TagDto;
import com.a302.webcuration.domain.Tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

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
    private final PostsRepository postsRepository;

    private final JwtService jwtService;

    //내정보
    public BaseMessage findAccountById(String token)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Long myId = jwtService.getAccountId(token);
            Account account = accountRepository.findAccountByAccountId(myId);
            logger.info("account = " + account.getAccountName());
            AccountDto.MyAccountProfile profile=profileMapping(account);
            //좋아요한 게시물 profile.set
            List<PostsDto.PostsWithOnePhoto> likePosts=new ArrayList<>();
            int likePostsCnt=account.getLikePosts().size();
            for (int idx=0;idx<likePostsCnt;idx++){
                Posts posts=account.getLikePosts().get(idx);
                PostsDto.PostsWithOnePhoto likePost= PostsDto.PostsWithOnePhoto.builder()
                        .postsId(posts.getPostsId())
                        .postsWriter(posts.getPostWriter().getAccountNickname())
                        .postsTitle(posts.getPostsTitle())
                        .postsPhoto(posts.getPostsPhotos().get(0))
                        .build();
                likePosts.add(likePost);
            }
            //정렬
            Collections.sort(likePosts, new Comparator<PostsDto.PostsWithOnePhoto>() {
                @Override
                public int compare(PostsDto.PostsWithOnePhoto o1, PostsDto.PostsWithOnePhoto o2) {
                    return (int)(o2.getPostsId()- o1.getPostsId());
                }
            });

            profile.setLikesPosts(likePosts);
            profile.setLikesPostsCnt(likePostsCnt);
            return  new BaseMessage(HttpStatus.OK, profile);

        }catch (Exception e)
        {
            resultMap.put("errors",e);
            logger.info("errors : ",e);
            return new BaseMessage(HttpStatus.BAD_REQUEST,resultMap);
        }
    }

    AccountDto.MyAccountProfile profileMapping(Account account) throws Exception{
        try {
            logger.info("test1 ");
            AccountDto.MyAccountProfile profile = modelMapper.map(account, AccountDto.MyAccountProfile.class);
            logger.info("test2 ");
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
                        .postsWriter(posts.getPostWriter().getAccountNickname())
                        .postsTitle(posts.getPostsTitle())
                        .postsPhoto(posts.getPostsPhotos().get(0))
                        .build();
                writtenPosts.add(writtenPost);
            }
            //정렬
            Collections.sort(writtenPosts, new Comparator<PostsDto.PostsWithOnePhoto>() {
                @Override
                public int compare(PostsDto.PostsWithOnePhoto o1, PostsDto.PostsWithOnePhoto o2) {
                    return (int)(o2.getPostsId()- o1.getPostsId());
                }
            });
            profile.setWrittenPosts(writtenPosts);
            profile.setWrittenPostsCnt(writtenPostsCnt);

            return profile;
        }catch (Exception e){
            logger.error("e.getMessage() "+ e.getMessage());
            throw e;
        }
    }


    public BaseMessage updateAccount(AccountDto.UpdateRequest request,String token) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Long myId = jwtService.getAccountId(token);
            Account account = accountRepository.findAccountByAccountId(myId);
            Account duplicateCheck=accountRepository.findAccountByAccountNickname(request.getAccountNickname());
            if(duplicateCheck==null){
                account.updateAccount(request);
                resultMap.put("message",account.getAccountName()+"님의 정보가 변경되었습니다.");
                return new BaseMessage( HttpStatus.OK,resultMap);
            }else if(duplicateCheck.getAccountEmail().equals(account.getAccountEmail())) {
                account.updateAccount(request);
                resultMap.put("message","기존과 동일한 닉네임입니다.");
                return new BaseMessage( HttpStatus.OK,resultMap);
            }else{
                resultMap.put("message","닉네임 중복입니다.");
                return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
            }
        }catch (Exception e)
        {
            logger.error(e.getMessage());
            resultMap.put("message","잘못된 값입니다.");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
    }


    public BaseMessage follow(Long myId, Long yourId){
        Map<String, Object> resultMap = new HashMap<>();
        logger.info("yourId : "+yourId);
        if(myId==yourId)
        {
            resultMap.put("message","자기 자신을 팔로우 할 수 없습니다.");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
        try {
            Account aAccount= accountRepository.findAccountByAccountId(myId);
            Account bAccount= accountRepository.findAccountByAccountId(yourId);
            aAccount.followAccount(bAccount);
            logger.debug("message",myId+"님이 "+yourId+" 를 팔로우하였습니다.");
            resultMap.put("message",myId+"님이 "+yourId+" 를 팔로우하였습니다.");
            return new BaseMessage(HttpStatus.OK,resultMap);
        }catch (Exception e)
        {
            resultMap.put("message","객체가 존재하지 않습니다.");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
    }

    public BaseMessage disconnect(Long myId, Long yourId){
        Map<String, Object> resultMap = new HashMap<>();
        logger.info("yourId : "+yourId);
        if(myId==yourId)
        {
            resultMap.put("message","자기 자신을 팔로우 취소 할 수 없습니다.");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
        try {
            Account aAccount= accountRepository.findAccountByAccountId(myId);
            Account bAccount= accountRepository.findAccountByAccountId(yourId);
            aAccount.disconnectAccount(bAccount);
            resultMap.put("message",myId+"님이 "+yourId+" 를 팔로우 취소하였습니다.");
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

    public BaseMessage deleteTag(Long tagId, String token) {
        Map<String, Object> resultMap = new HashMap<>();
        Long myId = jwtService.getAccountId(token);
        Account account=accountRepository.findAccountByAccountId(myId);
        Boolean deleteComplete=false;
        try {
            Tag tag= tagRepository.findTagByTagId(tagId);
            deleteComplete=account.deleteTag(tag);
            resultMap.put("delete",deleteComplete);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put("message","태그 삭제하는데 오류");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
        return new BaseMessage( HttpStatus.OK,deleteComplete);
    }

    //게시물 좋아요
    public BaseMessage likePosts(AccountDto.LikeRequest likeRequest, String token) {
        Map<String, Object> resultMap = new HashMap<>();
        Long myId = jwtService.getAccountId(token);
        Account account=accountRepository.findAccountByAccountId(myId);
        Posts posts=postsRepository.findPostsByPostsId(likeRequest.getPostId());
        if(posts!=null){
            if(account.likePosts(posts)){
                resultMap.put("message",account.getAccountName()+"회원님이 "+posts.getPostsTitle()+" 게시글을 좋아요했습니다");
                return new BaseMessage( HttpStatus.OK,resultMap);
            }else{
                resultMap.put("message","이미 좋아요한 게시물입니다.");
                return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
            }

        }else{
            resultMap.put("message","존재하지 않는 게시물입니다.");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
    }

    public BaseMessage cancelLikedPosts(Long postsId, String token) {
        Map<String, Object> resultMap = new HashMap<>();
        Long myId = jwtService.getAccountId(token);
        Account account=accountRepository.findAccountByAccountId(myId);
        Posts posts=postsRepository.findPostsByPostsId(postsId);
        if(posts!=null){
            if(account.cancelLikedPosts(posts)){
                resultMap.put("message",account.getAccountName()+"회원님이 "+posts.getPostsTitle()+" 게시글을 좋아요 취소했습니다");
                return new BaseMessage( HttpStatus.OK,resultMap);
            }else{
                resultMap.put("message","좋아요되어있지 않은 게시물입니다.");
                return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
            }

        }else{
            resultMap.put("message","존재하지 않는 게시물입니다.");
            return new BaseMessage( HttpStatus.BAD_REQUEST,resultMap);
        }
    }
}

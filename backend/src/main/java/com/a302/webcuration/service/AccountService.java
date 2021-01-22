package com.a302.webcuration.service;

import com.a302.webcuration.domain.Account.Account;
import com.a302.webcuration.domain.Account.AccountDto;
import com.a302.webcuration.domain.Account.AccountRepository;
import com.a302.webcuration.domain.Account.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;
    private final JwtService jwtService;

    @Value("${token.signiturekey}")
    private String signature ;
    public static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Transactional
    public List<AccountDto.CreateAccountResponse> findAll()
    {
        List<AccountDto.CreateAccountResponse> accounts = new ArrayList<>();
        for (Account account : accountRepository.findAll()) {
            accounts.add(modelMapper.map(account,AccountDto.CreateAccountResponse.class));
        }
        return accounts;
    }

    @Transactional
    public AccountDto.AccountProfile findAccountById(Long id)
    {
        Account account = accountRepository.findAccountByAccountId(id);
        System.out.println("account = " + account.getAccountName());
        AccountDto.AccountProfile profile = modelMapper.map(account,AccountDto.AccountProfile.class);

        List<AccountDto.FollowingDto> following = new ArrayList<>();
        List<AccountDto.FollowerDto> follower = new ArrayList<>();
        int followingCnt = account.getFollowing().size();
        int followerCnt = account.getFollower().size();
        Iterator<Account> iterFollower = account.getFollower().iterator();
        while(iterFollower.hasNext())
        {
            follower.add(modelMapper.map(iterFollower.next(),AccountDto.FollowerDto.class));
        }
        Iterator<Account> iterFollowing = account.getFollowing().iterator();
        while(iterFollowing.hasNext())
        {
            following.add(modelMapper.map(iterFollowing.next(),AccountDto.FollowingDto.class));
        }

        profile.setProfileFollower(follower);
        profile.setProfileFollowing(following);
        profile.setFollowerCnt(followerCnt);
        profile.setFollowingCnt(followingCnt);

        return profile;
    }

    public Long getAccountId(String tokenKey)
    {
        String token=tokenKey.substring(7);

        Jws<Claims> claims = null;

        try {
            claims = Jwts.parser().setSigningKey(signature.getBytes()).parseClaimsJws(token);
            logger.info("AccountId :"+claims.getBody().get("accountId"));
        } catch (final Exception e) {
            logger.info("복호화 실패");
            throw new RuntimeException();
        }
        return  Long.parseLong(claims.getBody().get("accountId").toString());
    }

    //TODO 구체적인 예외로 처리할 것
    public boolean followValidator(Long myId,Long yourId)
    {
        Account account = accountRepository.findAccountByAccountId(yourId);
        if(account==null)
        {
            return false;
        }
        if(myId==yourId)
        {
            return false;
        }
        if(yourId<0){
            return false;
        }
        return true;
    }

    @Transactional
    public void follow(Long myId,Long yourId){
        Account aAccount= accountRepository.findAccountByAccountId(myId);
        Account bAccount= accountRepository.findAccountByAccountId(yourId);
        aAccount.followAccount(bAccount);
    }

    //존재하는 이메일 여부 판정
    public void ExistEmailAddress(AccountDto.LoginRequest loginRequest, Errors errors)
    {
        Account account = accountRepository.findByAccountEmail(loginRequest.getAccountEmail());
        if(account==null)
        {
            errors.rejectValue("AccountEmail","doesn't exist","등록되지 않은 이메일입니다.");
        }
    }

    @Transactional
    //이메일 보내고 AuthKey Database에 저장
    public void login(AccountDto.LoginRequest loginRequest)
    {
        Account account = accountRepository.findByAccountEmail(loginRequest.getAccountEmail());
        String authCode = createEmailCode();
        //db에 저장
        account.changeAuthNum(authCode);
        sendMail(account.getAccountEmail(),authCode);
    }

    @Transactional
    public Map loginWithAuthKey(AccountDto.LoginAuthKeyRequest loginAuthKeyRequest) {
        Account account=accountRepository.findByAccountEmail(loginAuthKeyRequest.getAccountEmail());
        Map<String, Object> resultMap = new HashMap<>();
        if(account.getAccountAuthNum().equals(loginAuthKeyRequest.getAccountAuthNum())){
            logger.info("인증키 일치!");
            if(account.getAccountRole().equals(Role.TEMPORARY)){
                logger.info("처음으로 로그인된 계정입니다. 관심태그를 입력 받아야 합니다.");
                account.changeRole(Role.CERTIFICATED);
            }
            resultMap=loginInfo(account);
        }else{
            resultMap.put("message", "인증키가 일치하지 않습니다.");
        }
        return resultMap;
    }

    public Map loginInfo(Account account){
        Map<String, Object> resultMap = new HashMap<>();
        String token = "Bearer "+jwtService.create(account.getAccountId());
        logger.trace("로그인 토큰정보 : {}", token);
        resultMap.put("Authorization", token);
        resultMap.put("AccountId", account.getAccountId());
        resultMap.put("AccountEmail", account.getAccountEmail());
        return resultMap;
    }


    public void sendMail(String email,String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[PINSET] 로그인(이메일) 인증 코드 발송");
        message.setText("이메일 인증코드는 "+authCode+" 입니다.");
        System.out.println("message "+ message);
        mailSender.send(message);
    }

    private String createEmailCode() { //이메일 인증코드 생성
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String newCode = new String();

        for (int x = 0; x < 8; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }
        return newCode;
    }


}
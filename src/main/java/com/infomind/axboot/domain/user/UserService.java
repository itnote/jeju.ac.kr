package com.infomind.axboot.domain.user;

import com.chequer.axboot.core.code.AXBootTypes;
import com.infomind.axboot.domain.BaseService;
import com.infomind.axboot.domain.schedule.ScheduleMapper;
import com.infomind.axboot.domain.user.auth.UserAuth;
import com.infomind.axboot.domain.user.auth.UserAuthService;
import com.infomind.axboot.domain.user.role.UserRole;
import com.infomind.axboot.domain.user.role.UserRoleService;
import com.chequer.axboot.core.parameter.RequestParams;
import com.infomind.axboot.utils.SessionUtils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;


@Service
public class UserService extends BaseService<User, String> {

    private UserRepository userRepository;

    @Inject
    private UserAuthService userAuthService;

    @Inject
    private UserRoleService userRoleService;

    @Inject
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Inject
    private UserMapper userMapper;

    @Inject
    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveUser(List<User> users) throws Exception {
        if (isNotEmpty(users)) {
            for (User user : users) {
                delete(qUserRole).where(qUserRole.userCd.eq(user.getUserCd())).execute();
                delete(qUserAuth).where(qUserAuth.userCd.eq(user.getUserCd())).execute();

                String password = bCryptPasswordEncoder.encode("1");
                User originalUser = userRepository.findOne(user.getUserCd());

                if (originalUser != null) {
                    if (isNotEmpty(user.getUserPs())) {
                        user.setPasswordUpdateDate(Instant.now(Clock.systemUTC()));
                        user.setUserPs(password);
                    } else {
                        user.setUserPs(originalUser.getUserPs());
                    }
                } else {
                    user.setPasswordUpdateDate(Instant.now(Clock.systemUTC()));
                    user.setUserPs(password);
                }
                user.setInitYn(AXBootTypes.Used.YES);
                save(user);

                for (UserAuth userAuth : user.getAuthList()) {
                    userAuth.setUserCd(user.getUserCd());
                }

                for (UserRole userRole : user.getRoleList()) {
                    userRole.setUserCd(user.getUserCd());
                }

                userAuthService.save(user.getAuthList());
                userRoleService.save(user.getRoleList());
            }
        }
    }

    public User getUser(RequestParams requestParams) {
        User user = get(requestParams).stream().findAny().orElse(null);

        if (user != null) {
            user.setAuthList(userAuthService.get(requestParams));
            user.setRoleList(userRoleService.get(requestParams));
        }

        return user;
    }

    public List<User> get(RequestParams requestParams) {
        String userCd = requestParams.getString("userCd");
        String filter = requestParams.getString("filter");
        String schMenuGrpCd = requestParams.getString("schMenuGrpCd");

        BooleanBuilder builder = new BooleanBuilder();

        if (isNotEmpty(userCd)) {
            builder.and(qUser.userCd.eq(userCd));
        }
        if (isNotEmpty(schMenuGrpCd)) {
            builder.and(qUser.menuGrpCd.eq(schMenuGrpCd));
        }

        List<User> list = select().from(qUser).where(builder).orderBy(qUser.userNm.asc()).fetch();

        if (isNotEmpty(filter)) {
            list = filter(list, filter);
        }


        return list;
    }

    @Transactional
    public void saveLoginFailCnt(String userCd) throws Exception {
        User user = findOne(userCd);

        user.setLoginFailCnt(user.getLoginFailCnt() + 1);
        save(user);
    }

    @Transactional
    public void saveLoginFailCntToZero(String userCd) throws Exception {
        User user = findOne(userCd);

        user.setLoginFailCnt(0);
        save(user);
    }

    public List<User> getUserList(RequestParams requestParams){
        String userNm = requestParams.getString("userNm");

        if(userNm == null){
            userNm = "";
        }
        HashMap map = new HashMap<String, Object>();
        map.put("roleCd","USER");
        map.put("userNm",userNm);

        return userMapper.getUserList(map);
    }

    @Transactional
    public void resetPw(List<User> users) {
        for (User u : users) {
            User user = findOne(u.getUserCd());
            String password = bCryptPasswordEncoder.encode("1");
            user.setUserPs(password);
            user.setInitYn(AXBootTypes.Used.YES);
            user.setLoginFailCnt(0);
            save(user);
        }
    }

    @Transactional
    public void changePw(User users) {

        User user = findOne(SessionUtils.getCurrentLoginUserCd());
        String password = bCryptPasswordEncoder.encode(users.getUserPs());
        user.setUserPs(password);
        user.setInitYn(AXBootTypes.Used.NO);
        save(user);

    }
}

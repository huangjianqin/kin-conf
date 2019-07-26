package org.kin.conf.center.service.impl;

import com.alibaba.fastjson.JSON;
import org.kin.conf.center.dao.UserDao;
import org.kin.conf.center.domain.CommonResponse;
import org.kin.conf.center.domain.CookieKey;
import org.kin.conf.center.entity.User;
import org.kin.conf.center.service.UserService;
import org.kin.conf.center.utils.CookieUtils;
import org.kin.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Optional;

/**
 * @author huangjianqin
 * @date 2019/7/26
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private String makeToken(User user) {
        String tokenJson = JSON.toJSONString(user);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }

    private User parseToken(String tokenHex) {
        User user = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());      // username_password(md5)
            user = JSON.parseObject(tokenJson, User.class);
        }
        return user;
    }

    @Override
    public CommonResponse<String> login(HttpServletResponse response, String account, String password) {
        if (StringUtils.isBlank(account)) {
            return CommonResponse.fail("账号不能为空");
        }

        if (StringUtils.isBlank(password)) {
            return CommonResponse.fail("密码不能为空");
        }

        Optional<User> optional = userDao.findById(account);
        if (!optional.isPresent()) {
            return CommonResponse.fail("用户不存在");
        }

        User dbUser = optional.get();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!dbUser.getPassword().equals(password)) {
            return CommonResponse.fail("密码错误");
        }

        String token = makeToken(dbUser);
        CookieUtils.set(response, CookieKey.LOGIN_IDENTITY, token, true);

        return CommonResponse.success();
    }

    @Override
    public User isLogin(HttpServletRequest request) {
        String cookieToken = CookieUtils.getValue(request, CookieKey.LOGIN_IDENTITY);
        if (cookieToken != null) {
            User user = parseToken(cookieToken);
            if (user != null) {
                Optional<User> optional = userDao.findById(user.getAccount());
                if (optional.isPresent()) {
                    User dbUser = optional.get();
                    if (user.getPassword().equals(dbUser.getPassword())) {
                        return user;
                    }
                }
            }
        }
        return null;
    }
}

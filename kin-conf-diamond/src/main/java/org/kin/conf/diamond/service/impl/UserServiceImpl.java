package org.kin.conf.diamond.service.impl;

import org.kin.conf.diamond.dao.UserDao;
import org.kin.conf.diamond.domain.CookieKey;
import org.kin.conf.diamond.domain.WebResponse;
import org.kin.conf.diamond.entity.User;
import org.kin.conf.diamond.service.UserService;
import org.kin.conf.diamond.utils.CookieUtils;
import org.kin.framework.utils.JSON;
import org.kin.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author huangjianqin
 * @date 2019/7/26
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private String makeToken(User user) {
        String tokenJson = JSON.write(user);
        return new BigInteger(tokenJson.getBytes()).toString(16);
    }

    private User parseToken(String tokenHex) {
        User user = null;
        if (tokenHex != null) {
            // username_password(md5)
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());
            user = JSON.read(tokenJson, User.class);
        }
        return user;
    }

    @Override
    public WebResponse<String> login(HttpServletResponse response, String account, String password) {
        if (StringUtils.isBlank(account)) {
            return WebResponse.fail("账号不能为空");
        }

        if (StringUtils.isBlank(password)) {
            return WebResponse.fail("密码不能为空");
        }

        User dbUser = userDao.selectById(account);
        if (Objects.isNull(dbUser)) {
            return WebResponse.fail("用户不存在");
        }

        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!dbUser.getPassword().equals(password)) {
            return WebResponse.fail("密码错误");
        }

        String token = makeToken(dbUser);
        CookieUtils.set(response, CookieKey.LOGIN_IDENTITY, token, true);

        return WebResponse.success();
    }

    @Override
    public User getUser(HttpServletRequest request) {
        String cookieToken = CookieUtils.getValue(request, CookieKey.LOGIN_IDENTITY);
        if (StringUtils.isBlank(cookieToken)) {
            return null;
        }
        User user = parseToken(cookieToken);
        if (Objects.isNull(user)) {
            return null;
        }

        User dbUser = userDao.selectById(user.getAccount());
        if (Objects.nonNull(dbUser) && user.getPassword().equals(dbUser.getPassword())) {
            return user;
        }

        return null;
    }
}

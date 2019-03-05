package com.longyan.distribution.controller.management;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.User;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.UserLoginForm;
import com.longyan.distribution.request.UserPasswordForm;
import com.longyan.distribution.response.UserView;
import com.longyan.distribution.service.UserService;
import com.sug.core.platform.crypto.MD5;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;


@RestController
@RequestMapping(value = "/management")
public class CommonController {
    @Autowired
    private UserService userService;

    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(value = LOGIN, method = RequestMethod.POST)
    public ResponseView login(@Valid @RequestBody UserLoginForm form) {
        User user = userService.getByName(form.getName());
        if(Objects.isNull(user) ||
                !MD5.encrypt(form.getPassword() + MD5_SALT).equalsIgnoreCase(user.getPassword())){
            throw new ResourceNotFoundException("用户名或者密码错误");
        }
        sessionContext.setUser(user);
        return new ResponseView();
    }

    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    @UserLoginRequired
    public ResponseView resetPassword(@Valid @RequestBody UserPasswordForm form) {
        User user = sessionContext.getUser();
        if(!MD5.encrypt(form.getOriginPassword() + MD5_SALT).equalsIgnoreCase(user.getPassword())){
            throw new InvalidRequestException("密码错误");
        }
        user.setPassword(MD5.encrypt(form.getNewPassword() + MD5_SALT));
        user.setUpdateBy(user.getId());
        userService.update(user);
        sessionContext.setUser(user);
        return new ResponseView();
    }

    @RequestMapping(value = CURRENT, method = RequestMethod.GET)
    @UserLoginRequired
    public UserView current() {
        UserView view = new UserView();
        BeanUtils.copyProperties(sessionContext.getUser(),view);
        return view;
    }

    @RequestMapping(value = LOGOUT, method = RequestMethod.GET)
    public ResponseView logout() {
        sessionContext.logout();
        return new ResponseView();
    }
}

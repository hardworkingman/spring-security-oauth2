//package com.oauth2.demo.integration.authenticator.webchat.miniapp;
//
//import com.oauth2.demo.integration.authenticator.IntegrationAuthenticator;
//import com.zillion.cloud.api.user.entity.HlhloUser;
//import com.zillion.oauth2.integration.AuthType;
//import com.zillion.oauth2.integration.IntegrationAuthentication;
//import com.zillion.oauth2.integration.authenticator.IntegrationAuthenticator;
//import org.springframework.stereotype.Service;
//
///**
// * 小程序集成认证
// *
// * @author LIQIU
// * @date 2018-3-31
// **/
//@Service
//public class MiniAppIntegrationAuthenticator implements IntegrationAuthenticator {
//
//    public final static String MINIAPP_JOB = "wx_job";
//
//    public final static String MINIAPP_HR = "wx_hr";
///*
//    @Autowired
//    private WeixinAppProperties weixinAppProperties;
//
//    @Autowired
//    private WxMessageSender wxMessageSender;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private SocialService socialService;*/
//
//
//    @Override
//    public HlhloUser authenticate(IntegrationAuthentication integrationAuthentication) {
//        String username = integrationAuthentication.getAuthParameter("username");
//
//        HlhloUser user = null;
////        if (StringUtils.isBlank(username)) {//启动小程序时尝试登录
////            user = this.startLogin(integrationAuthentication);
////        } else {//加密手机号登录。
////            user = this.mobileLogin(integrationAuthentication);
////        }
//        return user;
//    }
//
//
//    @Override
//    public void prepare(IntegrationAuthentication integrationAuthentication) {
//
//    }
//
//    @Override
//    public boolean support(IntegrationAuthentication integrationAuthentication) {
//        return AuthType.SOCIAL_TYPE_WECHAT_MINIAPP.equals(integrationAuthentication.getAuthType());
//    }
//
//    @Override
//    public void complete(IntegrationAuthentication integrationAuthentication) {
//
//    }
//}

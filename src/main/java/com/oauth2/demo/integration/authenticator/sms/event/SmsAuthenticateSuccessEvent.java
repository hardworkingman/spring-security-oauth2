package com.oauth2.demo.integration.authenticator.sms.event;

import org.springframework.context.ApplicationEvent;

/**
 * 短信认证成功事件
 **/
public class SmsAuthenticateSuccessEvent extends ApplicationEvent {
    private static final long serialVersionUID = 6360433427236755832L;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public SmsAuthenticateSuccessEvent(Object source) {
        super(source);
    }
}

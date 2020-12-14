package com.pinyougou.user.controller;

/**
 * @author LLW
 * @date 2019/4/26
 */

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/** 短信发送工具类 */
public class SmsSendUtil {
    /** 产品名称:云通信短信API产品,开发者无需替换 */
    private static final String PRODUCT = "Dysmsapi";
    /** 产品域名,开发者无需替换 */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    // 签名KEY
//    private static final String ACCESS_KEY_ID = "LTAIiKVKFzm3Vsri";
    private static final String ACCESS_KEY_ID = "123";

    // 签名密钥
    private static final String ACCESS_KEY_SECRET = "Ki5bcJAPqoVH2iyJZ4alIIue6Ks5rS";
    // 短信模板ID: SMS_11480310
    private static final String TEMPLATE_CODE = "SMS_164266966";
    // 短信签名
    private static final String SIGN_NAME = "品优购";
    /**
     * 发送短信验证码方法
     * @param phoneNum 手机号码
     * @param verify 验证码
     * @return true: 成功 false: 失败
     */
    public static boolean send(String phoneNum, String verify){
        try {
            /** 可自助调整超时时间 */
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            /** 初始化acsClient,暂不支持region化 */
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                    ACCESS_KEY_ID,ACCESS_KEY_SECRET);
            /** cn-hangzhou: 中国.杭州 */
            DefaultProfile.addEndpoint("cn-hangzhou","cn-hangzhou",
                    PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            /** 组装请求对象*/
            SendSmsRequest request = new SendSmsRequest();
            // 必填: 待发送手机号
            request.setPhoneNumbers(phoneNum);
            // 必填: 短信签名-可在短信控制台中找到
            request.setSignName(SIGN_NAME);
            // 必填: 短信模板-可在短信控制台中找到
            request.setTemplateCode(TEMPLATE_CODE);
            /**
             * 可选: 模板中的变量替换JSON串,
             * 如模板内容为"亲爱的${name},您的验证码为${code}"
             */
            request.setTemplateParam("{\"code\":\"" + verify + "\"}");
            // hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            /** 判断短信是否发送成功 */
            return sendSmsResponse.getCode() != null &&
                    sendSmsResponse.getCode().equals("OK");
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public static void main(String[] args) {
        System.out.println(send("18379454088", "879657"));
    }
}

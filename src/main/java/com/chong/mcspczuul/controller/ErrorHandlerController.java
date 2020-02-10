package com.chong.mcspczuul.controller;

import com.chong.common.entity.ResponseData;
import com.chong.common.util.ResponseUtil;
import com.netflix.zuul.context.RequestContext;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/*
* 测试当zuulfilter发生异常时，错误会被ErrorController捕获。
*
* */
@Api(description = "错误控制器")
@RestController
public class ErrorHandlerController implements ErrorController {

    private final static Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);
    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath(){
        return "/error";
    }

    @ApiOperation(value="错误处理方法",notes="当zuulfilter里发生逾期外错误时执行错误处理")
    @ApiResponses({
            @ApiResponse(code=200,message = "成功信息",
                    response = ResponseData.class),
            @ApiResponse(code=-1,message = "预期外错误",
                    response = ResponseData.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name="request",value = "请求",
            paramType = "query", dataType = "HttpServletRequest",
                    required = true)
    })
    @RequestMapping(value = "/error",method = RequestMethod.GET)
    public ResponseData error(HttpServletRequest request){
        String ctxmsg1="ctx-errorHandlerController run ===";

        RequestContext context = RequestContext.getCurrentContext();
        Map<String,Object> map = errorAttributes.getErrorAttributes(new ServletWebRequest(request),true);
        String message = (String)map.get("message");
        String trace = (String)map.get("trace");

        String preHandleResult = (String)context.get("preFilterResult");
        context.set("preFilterResult", preHandleResult+"  "+ctxmsg1);

        logger.info((String)context.get("preFilterResult"));
        return ResponseUtil.fail("ErrorHanderMessage:"+message+"===ErrorHandlerTreace:"+trace);
    }
}

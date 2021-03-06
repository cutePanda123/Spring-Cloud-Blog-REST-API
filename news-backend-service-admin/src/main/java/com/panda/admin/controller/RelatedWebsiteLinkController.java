package com.panda.admin.controller;

import com.panda.admin.service.RelatedWebsiteLinkService;
import com.panda.api.controller.BaseController;
import com.panda.api.controller.admin.RelatedWebsiteLinkControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.RelatedLinkBo;
import com.panda.pojo.mo.RelatedLinkMo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
public class RelatedWebsiteLinkController extends BaseController implements RelatedWebsiteLinkControllerApi {
    @Autowired
    private RelatedWebsiteLinkService relatedWebsiteLinkService;

    @Override
    public ResponseResult saveOrUpdateRelatedLink(@Valid RelatedLinkBo bo, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            log.error(result.toString());
            return ResponseResult.errorMap(map);
        }
        relatedWebsiteLinkService.setRelatedWebsiteLink(bo);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult listRelatedLinks() {
        return ResponseResult.ok(relatedWebsiteLinkService.getAllLinks());
    }

    @Override
    public ResponseResult deleteLink(String linkId) {
        relatedWebsiteLinkService.deleteLink(linkId);
        return ResponseResult.ok();
    }

    @Override
    public ResponseResult listAliveLinks() {
        return ResponseResult.ok(relatedWebsiteLinkService.getAliveLinks());
    }
}

package com.panda.admin.service;

import com.panda.pojo.bo.RelatedLinkBo;

import java.util.List;

public interface RelatedWebsiteLinkService {
    public void saveOrUpdateRelatedWebsiteLink(RelatedLinkBo bo);
    public List<RelatedLinkBo> getAllLinks();
}

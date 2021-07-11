package com.panda.admin.service;

import com.panda.pojo.bo.RelatedLinkBo;

import java.util.List;

public interface RelatedWebsiteLinkService {
    public void setRelatedWebsiteLink(RelatedLinkBo bo);
    public List<RelatedLinkBo> getAllLinks();
    public void deleteLink(String linkId);
    public List<RelatedLinkBo> getAliveLinks();
}

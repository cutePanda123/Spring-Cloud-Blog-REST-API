package com.panda.admin.service.impl;

import com.panda.admin.mongodb.RelatedWebsiteLinkRepository;
import com.panda.admin.service.RelatedWebsiteLinkService;
import com.panda.pojo.bo.RelatedLinkBo;
import com.panda.pojo.mo.RelatedLinkMo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RelatedWebsiteLinkServiceImpl implements RelatedWebsiteLinkService {
    @Autowired
    private RelatedWebsiteLinkRepository relatedWebsiteLinkRepository;

    @Override
    public void saveOrUpdateRelatedWebsiteLink(RelatedLinkBo bo) {
        RelatedLinkMo mo = new RelatedLinkMo();
        BeanUtils.copyProperties(bo, mo);
        Date currentDate = new Date();
        mo.setCreateTime(currentDate);
        mo.setUpdateTime(currentDate);
        relatedWebsiteLinkRepository.insert(mo);
    }
}

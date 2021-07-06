package com.panda.admin.mongodb;

import com.panda.pojo.mo.RelatedLinkMo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RelatedWebsiteLinkRepository extends MongoRepository<RelatedLinkMo, String> {
}
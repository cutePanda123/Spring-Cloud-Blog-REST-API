package com.panda.admin.service.impl;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.panda.admin.mongodb.MongoClientConfig;
import com.panda.admin.service.RelatedWebsiteLinkService;
import com.panda.pojo.bo.RelatedLinkBo;
import com.panda.pojo.mo.RelatedLinkMo;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class RelatedWebsiteLinkServiceImpl implements RelatedWebsiteLinkService {
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private Sid sid;

    @Override
    public void saveOrUpdateRelatedWebsiteLink(RelatedLinkBo bo) {
        RelatedLinkMo mo = new RelatedLinkMo();
        BeanUtils.copyProperties(bo, mo);
        Date currentDate = new Date();
        mo.setCreateTime(currentDate);
        mo.setUpdateTime(currentDate);
        mo.setId(bo.getLinkUrl());

        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                org.bson.codecs.configuration.CodecRegistries.fromProviders(
                        PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase db = mongoClient.getDatabase(MongoClientConfig.MONGODB_NAME).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<RelatedLinkMo> linkCollection = db.getCollection(
                MongoClientConfig.RELATED_LINKS_COLLECTION_NAME,
                RelatedLinkMo.class
        );
        linkCollection.insertOne(mo);
    }

    @Override
    public List<RelatedLinkBo> getAllLinks() {
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                org.bson.codecs.configuration.CodecRegistries.fromProviders(
                        PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase db = mongoClient.getDatabase(MongoClientConfig.MONGODB_NAME).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<RelatedLinkMo> linkCollection = db.getCollection(
                MongoClientConfig.RELATED_LINKS_COLLECTION_NAME,
                RelatedLinkMo.class
        );
        FindIterable<RelatedLinkMo> mos = linkCollection.find();
        List<RelatedLinkBo> bos = new LinkedList<>();
        for (RelatedLinkMo mo : mos) {
            RelatedLinkBo bo = new RelatedLinkBo();
            BeanUtils.copyProperties(mo, bo);
            bos.add(bo);
        }
        return bos;
    }
}

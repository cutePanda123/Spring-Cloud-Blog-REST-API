package com.panda.user.service;

import com.panda.enums.Gender;
import com.panda.pojo.vo.FansRegionsCountsVo;
import com.panda.utils.PaginationResult;

import java.util.List;

public interface FansService {
    public boolean isFollowing(String writerId, String userId);

    public void follow(String writerId, String userId);

    public void unfollow(String writerId, String userId);

    public int getFollowingCount(String userId);

    public int getFansCount(String userId);

    public PaginationResult listFans(String writerId,
                                     Integer page,
                                     Integer pageSize);

    public Integer getFansCountByGender(String writerId, Gender gender);

    public List<FansRegionsCountsVo> getFansCountByRegion(String writerId);
}

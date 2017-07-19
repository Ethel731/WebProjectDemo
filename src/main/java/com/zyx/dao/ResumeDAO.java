package com.zyx.dao;

import com.zyx.model.Resume;
import com.zyx.model.ResumeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ResumeDAO {
    int countByExample(ResumeExample example);

    int deleteByExample(ResumeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Resume record);

    int insertSelective(Resume record);

    List<Resume> selectByExampleWithBLOBs(ResumeExample example);

    List<Resume> selectByExample(ResumeExample example);

    Resume selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Resume record, @Param("example") ResumeExample example);

    int updateByExampleWithBLOBs(@Param("record") Resume record, @Param("example") ResumeExample example);

    int updateByExample(@Param("record") Resume record, @Param("example") ResumeExample example);

    int updateByPrimaryKeySelective(Resume record);

    int updateByPrimaryKeyWithBLOBs(Resume record);

    int updateByPrimaryKey(Resume record);
}
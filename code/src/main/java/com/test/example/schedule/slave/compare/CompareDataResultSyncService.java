package com.test.example.schedule.slave.compare;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.example.code.compare.dao.CompareMybatisDao;
import com.test.example.code.compare.model.CompareResultSyncVO;
import com.test.example.code.compare.model.CompareResultVO;
import com.test.example.code.compare.service.CompareResultService;
import com.test.example.schedule.slave.common.AbstractMessageServiceListener;

/**
 *  项目比对结果同步
 * @author cg
 *
 */
@Component("compareDataResultSyncService")
public class CompareDataResultSyncService extends CompareServiceListener<CompareResultSyncVO>  {
	
	@Autowired
	private CompareResultService compareResultService;

	@Autowired
	private CompareMybatisDao<CompareResultVO> compareMybatisDao ;
	
	@Override
	protected List<CompareResultSyncVO> fetchDb(Long startKeyCode, Long endKeyCode) {
		return  compareMybatisDao.getCompareResultSyncVOList(startKeyCode, endKeyCode);
	}

	@Override
	public void doService(List<CompareResultSyncVO> objList) {
		for (CompareResultSyncVO compareResultSyncVO : objList) {
			String type = ObjectUtils.toString(compareResultSyncVO.getType());
			String prpCode = ObjectUtils.toString(compareResultSyncVO.getPrpCode());
			compareResultService.callUpdatePrpExtendCnt(type, prpCode);
		}
	}
	
}

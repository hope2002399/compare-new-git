package com.test.example.schedule.slave.compare;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.compare.dao.CompareMybatisDao;
import com.test.example.code.compare.model.CompareResultVO;
import com.test.example.code.compare.service.CompareResultService;
import com.test.example.code.compare.service.CompareTemplateService;
import com.test.example.schedule.slave.common.AbstractMessageServiceListener;

/**
 *  项目比较
 * @author cg
 *
 */
@Component("compareDataSimiliarService")
public class CompareDataSimiliarService  extends CompareServiceListener<CompareResultVO>  implements InitializingBean{
	
	private Map<String, CompareTemplateService> templateMap = null;
	
	@Autowired
	private CompareMybatisDao<CompareResultVO> compareMybatisDao ;

	@Autowired
  private CompareResultService compareResultService;

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly=true )
	protected List<CompareResultVO> fetchDb(Long startKeyCode, 	Long endKeyCode) {
		return compareMybatisDao.getCompareResultList(startKeyCode, endKeyCode);
	}

	@Override
	public void doService(List<CompareResultVO> list ) {
			compareResultService.calculateSimilaryAndUpdateCompareResultBatch(list , templateMap);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		 templateMap = compareResultService.getCompareTemplateAll();
	}

}

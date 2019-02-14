package com.longyan.distribution;

import com.longyan.distribution.domain.SystemParams;
import com.longyan.distribution.mapper.CoinRecordMapper;
import com.longyan.distribution.service.SystemParamsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;

import static com.longyan.distribution.constants.SystemParamsConstants.INVITECOMMONBECOMEVIPCOIN;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributionApplicationTests {

	@Autowired
	private SystemParamsService systemParamsService;

	@Autowired
	private CoinRecordMapper coinRecordMapper;
	@Test
	public void contextLoads() {
//		SystemParams s = systemParamsService.getValueByKey(Collections.singletonMap("key",COMMONCHARGE));
//		System.out.println(s.getValue()+"ERROR");
//		coinRecordMapper.selectCoinRecordAndCustomerList(Collections.singletonMap("type",1));
//		BigDecimal decimal = new BigDecimal(123);
//		decimal = decimal.multiply(new BigDecimal(-1));
//		System.out.println(decimal);
		System.out.println(coinRecordMapper.selectCoinRecordAndCustomerList(Collections.singletonMap("type",1)).size());
	}


	@Test
	public void test(){
		System.out.println(String.format("%06d",1));
		System.out.println(String.format("%06d",111111111));
	}
}


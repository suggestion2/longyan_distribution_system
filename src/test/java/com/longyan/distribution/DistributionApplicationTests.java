package com.longyan.distribution;

import com.longyan.distribution.domain.SystemParams;
import com.longyan.distribution.service.SystemParamsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributionApplicationTests {

	@Autowired
	private SystemParamsService systemParamsService;
	@Test
	public void contextLoads() {
//		SystemParams s = systemParamsService.getValueByKey(Collections.singletonMap("key",COMMONCHARGE));
//		System.out.println(s.getValue()+"ERROR");
		BigDecimal decimal = new BigDecimal(123);
		decimal = decimal.multiply(new BigDecimal(-1));
		System.out.println(decimal);
	}

}


package com.bluebid.auction_app_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import com.bluebid.auction_app_service.config.WebSocketConfig;


@SpringBootTest
@ContextConfiguration(classes = {WebSocketConfig.class})
class AuctionAppServiceApplicationTests {
	@Test
	void contextLoads() {
	}

}

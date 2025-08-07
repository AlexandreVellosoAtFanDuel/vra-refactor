package com.betfair.video.api;

import com.betfair.video.api.infra.client.CROClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class VideoRetrievalApiApplicationTests {

	@MockitoBean
	private CROClient croClient;

	@Test
	void contextLoads() {
	}

}

package com.goodformentertainment.canary.xis;

import static org.easymock.EasyMock.*;

import java.util.Collection;
import java.util.HashSet;

import net.canarymod.logger.Logman;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

public class BlockScoreValueTest extends EasyMockSupport {
	private XConfig mockConfig;
	private Logman mockLog;
	private BlockScoreValue blockScoreValues;
	
	@Before
	public void init() {
		mockConfig = createMock(XConfig.class);
		mockLog = createMock(Logman.class);
		XPlugin.LOG = mockLog;
	}
	
	@Test
	public void test() {
		final Collection<String> ignoredRemoves = new HashSet<String>();
		ignoredRemoves.add("cobblestone");
		ignoredRemoves.add("tree");
		
		expect(mockConfig.getIgnoredBlockRemoves()).andReturn(ignoredRemoves);
		expect(mockConfig.getBlockTypeValue(isA(String.class))).andReturn(2).anyTimes();
		expect(mockConfig.getBlockVariantValueMultiplier(isA(String.class))).andReturn(2).anyTimes();
		expect(mockConfig.getBlockColorValueMultiplier(isA(String.class))).andReturn(2).anyTimes();
		mockLog.debug(isA(String.class));
		expectLastCall().anyTimes();
		
		replayAll();
		
		blockScoreValues = new BlockScoreValue(mockConfig);
		
		System.out.println("Place Values size:\n" + blockScoreValues.getPlaceValues().size());
		System.out.println("Remove Values size:\n" + blockScoreValues.getRemoveValues().size());
		
		verifyAll();
	}
}

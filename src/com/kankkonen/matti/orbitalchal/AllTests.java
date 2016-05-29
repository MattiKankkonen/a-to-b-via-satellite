package com.kankkonen.matti.orbitalchal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ NetworkTest.class, NodeTest.class, PathFinderTest.class })
public class AllTests {

}

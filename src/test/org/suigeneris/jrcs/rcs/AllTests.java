/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: AllTests.java,v 1.3 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase
{

    public AllTests(String s)
    {
        super(s);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ArchiveTest.class);
        suite.addTestSuite(ChangeDeltaTest.class);
        suite.addTestSuite(KeywordsFormatTest.class);
        suite.addTestSuite(ParsingTest.class);
        suite.addTestSuite(BinaryTests.class);
        return suite;
    }
}

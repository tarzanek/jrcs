/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: AllTests.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff;

import junit.framework.*;

public class AllTests extends TestCase
{

    public AllTests(String s)
    {
        super(s);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(SimpleDiffTests.class);
        suite.addTestSuite(MyersDiffTests.class);
        return suite;
    }
}

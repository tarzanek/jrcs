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

package org.suigeneris.jrcs;

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
        suite.addTest(org.suigeneris.jrcs.diff.AllTests.suite());
        suite.addTest(org.suigeneris.jrcs.rcs.AllTests.suite());
        return suite;
    }
}

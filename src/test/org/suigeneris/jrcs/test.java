/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: test.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
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

public class test
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(org.suigeneris.jrcs.diff.DiffTest.suite());
        suite.addTest(org.suigeneris.jrcs.rcs.ArchiveTest.suite());
        return suite;
    }

    public static void main(String args[])
    {
        // junit.swingui.TestRunner.main(new String[] {"tests"});
        junit.textui.TestRunner.main(new String[]
        { "test" });
    }

}

/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: SimpleDiffTests.java,v 1.3 2006/06/08 05:28:34 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff;

import org.suigeneris.jrcs.diff.simple.SimpleDiff;

public class SimpleDiffTests extends DiffTest
{

    public SimpleDiffTests(String name)
    {
        super(name, new SimpleDiff());
    }

}

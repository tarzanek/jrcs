/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: InvalidVersionNumberException.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

/**
 * Thrown whenever a given version number is invalid for the context. This class
 * is NOT thread safe.
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: InvalidVersionNumberException.java,v 1.3 2003/10/13 07:59:46
 *          rdonkin Exp $
 */

public class InvalidVersionNumberException extends IllegalArgumentException
{
    public InvalidVersionNumberException()
    {
    }

    public InvalidVersionNumberException(String v)
    {
        super(v);
    }

    public InvalidVersionNumberException(Version v)
    {
        super(v.toString());
    }
}

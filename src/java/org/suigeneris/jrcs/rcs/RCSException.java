/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: RCSException.java,v 1.3 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

public class RCSException extends Exception
{

    public RCSException()
    {
    }

    public RCSException(String v)
    {
        super(v);
    }

    public RCSException(Version v)
    {
        super(v.toString());
    }

    public RCSException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RCSException(Throwable cause)
    {
        super(cause);
    }

}

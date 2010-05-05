/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: InvalidFileFormatException.java,v 1.3 2006/06/08 01:22:27 juanca Exp $
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
 * Thrown if the ArchiveParser cannot parse a given archive. This class is NOT
 * thread safe.
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: InvalidFileFormatException.java,v 1.3 2003/10/13 07:59:46
 *          rdonkin Exp $
 */
public class InvalidFileFormatException extends RCSException
{

    public InvalidFileFormatException()
    {
    }

    public InvalidFileFormatException(String msg)
    {
        super(msg);
    }

    public InvalidFileFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidFileFormatException(Throwable cause)
    {
        super(cause);
    }

    public InvalidFileFormatException(Version v)
    {
        super(v);
    }
}

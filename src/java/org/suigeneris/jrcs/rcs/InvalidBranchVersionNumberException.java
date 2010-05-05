/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: InvalidBranchVersionNumberException.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
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
 * Thrown if the version number given for a branch node is invalid. Branch
 * version numbers must be of the form (x.y)+, tha is, they must have an even
 * number of version digits, and an odd number of dots. This class is NOT thread
 * safe.
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: InvalidBranchVersionNumberException.java,v 1.3 2003/10/13
 *          07:59:46 rdonkin Exp $
 */
public class InvalidBranchVersionNumberException extends
        InvalidVersionNumberException
{

    public InvalidBranchVersionNumberException()
    {
    }

    public InvalidBranchVersionNumberException(String v)
    {
        super(v);
    }

    public InvalidBranchVersionNumberException(Version v)
    {
        super(v);
    }

}

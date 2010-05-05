/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: HeadAlreadySetException.java,v 1.3 2006/06/08 15:07:10 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs;

import org.suigeneris.jrcs.rcs.parse.ArchiveParser;

/**
 * Thrown if the ArchiveParser finds that the head node is set more than once.
 * This class is NOT thread safe.
 * 
 * @see Archive
 * @see ArchiveParser
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: HeadAlreadySetException.java,v 1.3 2003/10/13 07:59:46 rdonkin
 *          Exp $
 */
public class HeadAlreadySetException extends IllegalArgumentException
{
    public HeadAlreadySetException()
    {
    }

    public HeadAlreadySetException(String v)
    {
        super(v);
    }

    public HeadAlreadySetException(Version v)
    {
        super(v.toString());
    }
}

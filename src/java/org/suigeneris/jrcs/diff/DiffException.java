/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: DiffException.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff;

/**
 * Base class for all exceptions emanating from this package.
 * 
 * @version $Revision: 1.2 $ $Date: 2006/06/08 01:22:27 $
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 */
public class DiffException extends Exception
{

    public DiffException()
    {
    }

    public DiffException(String msg)
    {
        super(msg);
    }
}

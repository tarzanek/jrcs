/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: DifferentiationFailedException.java,v 1.2 2006/06/08 01:22:27 juanca Exp $
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
 * Thrown whenever the differencing engine cannot produce the differences
 * between two revisions of ta text.
 * 
 * @version $Revision: 1.2 $ $Date: 2006/06/08 01:22:27 $
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @see Diff
 * @see DiffAlgorithm
 */
public class DifferentiationFailedException extends DiffException
{

    public DifferentiationFailedException()
    {
    }

    public DifferentiationFailedException(String msg)
    {
        super(msg);
    }
}

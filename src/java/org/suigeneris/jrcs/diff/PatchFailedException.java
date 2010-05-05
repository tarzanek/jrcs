/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: PatchFailedException.java,v 1.3 2006/06/08 05:28:33 juanca Exp $
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

import org.suigeneris.jrcs.diff.delta.Delta;

/**
 * Thrown whenever a delta cannot be applied as a patch to a given text.
 * 
 * @version $Revision: 1.3 $ $Date: 2006/06/08 05:28:33 $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @see Delta
 * @see Diff
 */
public class PatchFailedException extends DiffException
{

    public PatchFailedException()
    {
    }

    public PatchFailedException(String msg)
    {
        super(msg);
    }
}

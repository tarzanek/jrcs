/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: NodeNotFoundException.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs.impl;

import org.suigeneris.jrcs.rcs.Version;

public class NodeNotFoundException extends IllegalArgumentException
{
    public NodeNotFoundException()
    {
        super();
    }

    public NodeNotFoundException(String msg)
    {
        super(msg);
    }

    public NodeNotFoundException(Version v)
    {
        super(v.toString());
    }
}

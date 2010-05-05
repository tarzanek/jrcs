/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: Line.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.rcs.impl;

import org.suigeneris.jrcs.rcs.Archive;

/**
 * An annotated line of a revision. Line contains both the original text of the
 * line, plus the node that indicates the revision in which the line was last
 * added or changed. This class is NOT thread safe.
 * 
 * @see Node
 * @see Archive
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: Line.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
 */
final public class Line
{
    private final Node revision;
    private final Object text;

    Line(Node revision, Object text)
    {
        this.text = text;
        this.revision = revision;
    }

    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        else if (!(other instanceof Line))
        {
            return false;
        }
        else
        {
            return this.getText().equals(((Line) other).getText());
        }
    }

    public int hashCode()
    {
        return getText().hashCode();
    }

    final Node getRevision()
    {
        return revision;
    }

    final Object getText()
    {
        return text;
    }
}

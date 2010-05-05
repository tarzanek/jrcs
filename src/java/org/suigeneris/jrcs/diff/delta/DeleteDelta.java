/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: DeleteDelta.java,v 1.1 2006/06/08 05:28:34 juanca Exp $
 *
 * For usage rights please see the LICENSE.* files that 
 * come bundled with legal distributions of this resource.
 *  
 * Fore more information please email Juancarlo Añez at:
 *
 *      juanca@suigeneris.org
 *      juancarlo.anez@gmail.com
 */

package org.suigeneris.jrcs.diff.delta;

import java.util.List;

import org.suigeneris.jrcs.diff.Diff;
import org.suigeneris.jrcs.diff.PatchFailedException;
import org.suigeneris.jrcs.diff.RevisionVisitor;

/**
 * Holds a delete-delta between to revisions of a text.
 * 
 * @version $Id: DeleteDelta.java,v 1.1 2006/06/08 05:28:34 juanca Exp $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @see Delta
 * @see Diff
 * @see Chunk
 */
public class DeleteDelta extends Delta
{

    DeleteDelta()
    {
        super();
    }

    public DeleteDelta(Chunk orig)
    {
        init(orig, null);
    }

    public void verify(List target) throws PatchFailedException
    {
        if (!original.verify(target))
        {
            throw new PatchFailedException();
        }
    }

    public void applyTo(List target)
    {
        original.applyDelete(target);
    }

    public void toString(StringBuffer s)
    {
        s.append(original.rangeString());
        s.append("d");
        s.append(revised.rcsto());
        s.append(Diff.NL);
        original.toString(s, "< ", Diff.NL);
    }

    public void toRCSString(StringBuffer s, String EOL)
    {
        s.append("d");
        s.append(original.rcsfrom());
        s.append(" ");
        s.append(original.size());
        s.append(EOL);
    }

    public void accept(RevisionVisitor visitor)
    {
        visitor.visit(this);
    }
}
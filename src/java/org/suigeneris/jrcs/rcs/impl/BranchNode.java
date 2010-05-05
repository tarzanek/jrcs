/*
 * Copyright (c) 1999-2006 Juancarlo Añez
 * All rights reserved.
 *
 * $Id: BranchNode.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
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
import org.suigeneris.jrcs.rcs.BranchNotFoundException;
import org.suigeneris.jrcs.rcs.Version;

/**
 * Represents a branch node in a version control archive. This class is NOT
 * thread safe.
 * 
 * <p>
 * A {@link BranchNode BranchNode} stores the deltas between the previous
 * revision and the current revision; that is, when the deltas are applied to
 * the previous revision, the text of the current revision is obtained. The
 * {@link Node#rcsnext rcsnext} field of a BranchNode points to the next
 * revision in the branch.
 * </p>
 * 
 * @see Node
 * @see Archive
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: BranchNode.java,v 1.1 2006/06/08 15:07:11 juanca Exp $
 */
public class BranchNode extends Node
{
    /**
     * Create a BranchNode with the given version number. The next field in a
     * Branch node points to the next higher revision on the same branch.
     * 
     * @param vernum
     *            the version number for the node
     * @param next
     *            the next node in the logical RCS hierarchy.
     */
    BranchNode(Version vernum, BranchNode next)
    {
        super(vernum, next);
        if (vernum == null)
        {
            throw new IllegalArgumentException(vernum.toString());
        }
    }

    /**
     * Return the last (leaf) node in the branch this node belongs to.
     * 
     * @return The leaf node.
     */
    public BranchNode getLeafNode()
    {
        BranchNode result = this;
        while (result.getRCSNext() != null)
        {
            result = (BranchNode) result.getRCSNext();
        }
        return result;
    }

    /**
     * Set the next node in the RCS logical hierarcy. Update the _parent and
     * _child node accordingly. For BranchNodes, the RCS-next is a child, that
     * is, a node with a larger version number.
     */
    public void setRCSNext(Node node)
    {
        super.setRCSNext(node);
        if (this.getChild() != null)
        {
            this.getChild().parent = null;
        }
        this.child = node;
        if (this.getChild() != null)
        {
            this.getChild().parent = this;
        }
    }

    public Node deltaRevision()
    {
        return this;
    }

    public Node nextInPathTo(Version vernum, boolean soft)
            throws NodeNotFoundException
    {
        Version branchPoint = vernum.getBase(this.version.size());
        Version thisBase = this.version.getBase(branchPoint.size());
        if (thisBase.isGreaterThan(branchPoint) && !soft)
        {
            throw new NodeNotFoundException(vernum);
        } // !!! InternalError, really

        if (this.version.equals(vernum))
        {
            return null;
        }
        else if (this.version.isLessThan(branchPoint))
        {
            return getChild();
        }
        else if (vernum.size() <= this.version.size())
        {
            if (vernum.size() < this.version.size() || branchPoint.last() == 0)
            {
                return getChild();
            } // keep going
            else
            {
                return null;
            }
        }
        else
        {
            Node branch = getBranch(vernum.at(this.version.size()));
            if (branch != null || soft)
            {
                return branch;
            }
            else
            {
                throw new BranchNotFoundException(vernum.getBase(this.version
                        .size() + 1));
            }
        }
    }
}

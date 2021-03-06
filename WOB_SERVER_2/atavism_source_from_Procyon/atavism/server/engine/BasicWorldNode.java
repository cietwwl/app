// 
// Decompiled by Procyon v0.5.30
// 

package atavism.server.engine;

import atavism.server.util.Log;
import atavism.server.math.Quaternion;
import atavism.server.math.AOVector;
import atavism.server.math.Point;
import java.io.Serializable;

public class BasicWorldNode implements IBasicWorldNode, Serializable
{
    protected OID instanceOid;
    protected Point loc;
    protected AOVector dir;
    protected Quaternion orient;
    private static final long serialVersionUID = 1L;
    
    public BasicWorldNode() {
        this.loc = null;
        this.dir = null;
        this.orient = null;
        this.setupTransient();
    }
    
    public BasicWorldNode(final InterpolatedWorldNode inode) {
        this.loc = null;
        this.dir = null;
        this.orient = null;
        this.setupTransient();
        this.instanceOid = inode.getInstanceOid();
        this.loc = inode.getLoc();
        this.dir = inode.getDir();
        this.orient = inode.getOrientation();
    }
    
    public BasicWorldNode(final OID instanceOid, final AOVector dir, final Point loc, final Quaternion orient) {
        this.loc = null;
        this.dir = null;
        this.orient = null;
        this.setupTransient();
        this.instanceOid = instanceOid;
        this.dir = dir;
        this.loc = loc;
        this.orient = orient;
    }
    
    @Override
    public String toString() {
        return "BasicWorldNode[instanceOid=" + this.instanceOid + " loc=" + this.loc + " dir=" + this.dir + " orient=" + this.orient + "]";
    }
    
    @Override
    public boolean equals(final Object obj) {
        final BasicWorldNode other = (BasicWorldNode)obj;
        Log.debug("BWN equals with instanceOid: " + this.instanceOid + " and other instanceOid: " + other.instanceOid);
        return (this.instanceOid == null || this.instanceOid.compareTo(other.instanceOid) == 0) && this.loc.equals(other.loc) && this.orient.equals(other.orient) && this.dir.equals(other.dir);
    }
    
    protected void setupTransient() {
    }
    
    @Override
    public OID getInstanceOid() {
        return this.instanceOid;
    }
    
    @Override
    public void setInstanceOid(final OID oid) {
        this.instanceOid = oid;
    }
    
    @Override
    public Point getLoc() {
        return this.loc;
    }
    
    @Override
    public void setLoc(final Point loc) {
        this.loc = loc;
    }
    
    @Override
    public Quaternion getOrientation() {
        return this.orient;
    }
    
    @Override
    public void setOrientation(final Quaternion orient) {
        this.orient = orient;
    }
    
    @Override
    public AOVector getDir() {
        return this.dir;
    }
    
    @Override
    public void setDir(final AOVector dir) {
        this.dir = dir;
    }
}

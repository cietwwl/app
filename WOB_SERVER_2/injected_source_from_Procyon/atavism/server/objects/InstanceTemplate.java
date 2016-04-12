// 
// Decompiled by Procyon v0.5.30
// 

package atavism.server.objects;

import atavism.server.marshalling.MarshallingRuntime;
import atavism.server.network.AOByteBuffer;
import atavism.server.plugins.MobManagerClient;
import atavism.agis.plugins.AgisMobPlugin;
import atavism.server.plugins.InstanceClient;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import atavism.server.engine.Engine;
import java.util.Collection;
import atavism.server.plugins.ObjectManagerClient;
import java.io.Serializable;
import atavism.server.plugins.WorldManagerClient;
import atavism.server.engine.Namespace;
import atavism.server.math.Quaternion;
import atavism.server.math.Point;
import atavism.server.util.Log;
import java.util.concurrent.ScheduledFuture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import atavism.server.engine.OID;
import atavism.server.marshalling.Marshallable;

public class InstanceTemplate implements Runnable, Marshallable
{
    int id;
    int category;
    String name;
    OID administrator;
    boolean isPublic;
    String password;
    LinkedList<OID> developers;
    int islandType;
    boolean createOnStartup;
    int rating;
    String style;
    String description;
    int size;
    int populationLimit;
    LinkedList<String> contentPacks;
    boolean subscriptionActive;
    HashMap<String, HashMap<String, Float>> portals;
    HashMap<Integer, SpawnData> spawns;
    ArrayList<OID> instanceOids;
    HashMap<OID, ArrayList<SpawnData>> instanceSpawns;
    ArrayList<ScheduledFuture> spawnTasks;
    public static final int ISLAND_TYPE_WORLD = 1;
    public static final int ISLAND_TYPE_DUNGEON = 2;
    public static final int ISLAND_TYPE_ARENA = 3;
    public static final int ISLAND_TYPE_HOUSE = 4;
    private static final long serialVersionUID = 1L;
    
    public InstanceTemplate() {
        this.populationLimit = -1;
        this.instanceOids = new ArrayList<OID>();
        this.instanceSpawns = new HashMap<OID, ArrayList<SpawnData>>();
        this.spawnTasks = new ArrayList<ScheduledFuture>();
        this.developers = new LinkedList<OID>();
        this.description = "";
        this.style = "";
        this.islandType = 0;
        this.password = "";
        this.rating = 0;
    }
    
    public void scheduleSpawnLoading(final OID instanceOid) {
        Log.debug("INSTANCE: scheduling spawn loading for instance: " + instanceOid);
        for (final String portalName : this.portals.keySet()) {
            final HashMap<String, Float> portalProps = this.portals.get(portalName);
            final Template markerTemplate = new Template();
            final Point loc = new Point(portalProps.get("locX"), portalProps.get("locY") + 0.0f, portalProps.get("locZ"));
            final Quaternion orient = new Quaternion(portalProps.get("orientX"), portalProps.get("orientY"), portalProps.get("orientZ"), portalProps.get("orientW"));
            markerTemplate.put(Namespace.WORLD_MANAGER, WorldManagerClient.TEMPL_NAME, portalName);
            markerTemplate.put(Namespace.WORLD_MANAGER, WorldManagerClient.TEMPL_OBJECT_TYPE, (Serializable)WorldManagerClient.TEMPL_OBJECT_TYPE_MARKER);
            markerTemplate.put(Namespace.WORLD_MANAGER, WorldManagerClient.TEMPL_INSTANCE, (Serializable)instanceOid);
            markerTemplate.put(Namespace.WORLD_MANAGER, WorldManagerClient.TEMPL_LOC, loc);
            markerTemplate.put(Namespace.WORLD_MANAGER, WorldManagerClient.TEMPL_ORIENT, orient);
            final OID objOid = ObjectManagerClient.generateObject(-1, "BaseTemplate", markerTemplate);
            if (objOid != null) {
                WorldManagerClient.spawn(objOid);
                Log.debug("PORTAL: spawned portal " + portalName + " in instance " + this.name);
            }
        }
        this.instanceOids.add(instanceOid);
        this.instanceSpawns.put(instanceOid, new ArrayList<SpawnData>(this.spawns.values()));
        this.spawnTasks.add(Engine.getExecutor().scheduleAtFixedRate(this, 10000L, 300L, TimeUnit.MILLISECONDS));
    }
    
    @Override
    public void run() {
        Log.debug("INSTANCE: spawning mobs for instance: " + this.instanceOids.get(0));
        final OID instanceOid = this.instanceOids.get(0);
        final ArrayList<SpawnData> instanceSpawnList = this.instanceSpawns.get(instanceOid);
        this.loadSpawn(instanceSpawnList.remove(0), instanceOid);
        if (instanceSpawnList.size() == 0) {
            this.instanceSpawns.remove(instanceOid);
            this.instanceOids.remove(0);
            this.spawnTasks.remove(0).cancel(true);
            Log.debug("INSTANCE: spawning task finished");
        }
    }
    
    protected void loadSpawns(final OID instanceOid) {
        for (final SpawnData sd : this.spawns.values()) {
            this.loadSpawn(sd, instanceOid);
        }
    }
    
    protected void loadSpawn(final SpawnData sd, final OID instanceOid) {
        Log.debug("TEST: spawnData:" + sd);
        final String location = sd.getStringProperty("markerName");
        Log.debug("TEST: location:" + location);
        if (location != null && !location.equals("")) {
            final Marker m = InstanceClient.getMarker(instanceOid, location);
            sd.setLoc(m.getPoint());
            sd.setOrientation(m.getOrientation());
        }
        Log.debug("MOB: finished location setting for spawn for mob: " + this.name);
        sd.setInstanceOid(instanceOid);
        final String factoryName = AgisMobPlugin.createMobFactory(sd);
        if (!factoryName.equals("")) {
            sd.setFactoryName(factoryName);
            MobManagerClient.createSpawnGenerator(sd);
        }
    }
    
    public void updatePortal(final String portalName, final HashMap<String, Float> portalProps) {
        this.portals.put(portalName, portalProps);
    }
    
    public int getID() {
        return this.id;
    }
    
    public void setID(final int id) {
        this.id = id;
    }
    
    public int getCategory() {
        return this.category;
    }
    
    public void setCategory(final int category) {
        this.category = category;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public OID getAdministrator() {
        return this.administrator;
    }
    
    public void setAdministrator(final OID administrator) {
        this.administrator = administrator;
    }
    
    public boolean getIsPublic() {
        return this.isPublic;
    }
    
    public void setIsPublic(final boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public LinkedList<OID> getDevelopers() {
        return this.developers;
    }
    
    public void setDevelopers(final LinkedList<OID> developers) {
        this.developers = developers;
    }
    
    public int getIslandType() {
        return this.islandType;
    }
    
    public void setIslandType(final int islandType) {
        this.islandType = islandType;
    }
    
    public boolean getCreateOnStartup() {
        return this.createOnStartup;
    }
    
    public void setCreateOnStartup(final boolean createOnStartup) {
        this.createOnStartup = createOnStartup;
    }
    
    public int getRating() {
        return this.rating;
    }
    
    public void setRating(final int rating) {
        this.rating = rating;
    }
    
    public String getStyle() {
        return this.style;
    }
    
    public void setStyle(final String style) {
        this.style = style;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
    
    public int getPopulationLimit() {
        return this.populationLimit;
    }
    
    public void setPopulationLimit(final int limit) {
        this.populationLimit = limit;
    }
    
    public LinkedList<String> getContentPacks() {
        return this.contentPacks;
    }
    
    public void setContentPacks(final LinkedList<String> contentPacks) {
        this.contentPacks = contentPacks;
    }
    
    public boolean getSubscriptionActive() {
        return this.subscriptionActive;
    }
    
    public void setSubscriptionActive(final boolean subscriptionActive) {
        this.subscriptionActive = subscriptionActive;
    }
    
    public HashMap<String, HashMap<String, Float>> getPortals() {
        return this.portals;
    }
    
    public void setPortals(final HashMap<String, HashMap<String, Float>> portals) {
        this.portals = portals;
    }
    
    public HashMap<Integer, SpawnData> getSpawns() {
        return this.spawns;
    }
    
    public void setSpawns(final HashMap<Integer, SpawnData> spawns) {
        this.spawns = spawns;
    }
    
    public void marshalObject(final AOByteBuffer buf) {
        byte flag_bits = 0;
        if (this.name != null && this.name != "") {
            flag_bits = 1;
        }
        if (this.administrator != null) {
            flag_bits |= 0x2;
        }
        if (this.password != null && this.password != "") {
            flag_bits |= 0x4;
        }
        if (this.developers != null) {
            flag_bits |= 0x8;
        }
        if (this.style != null && this.style != "") {
            flag_bits |= 0x10;
        }
        if (this.description != null && this.description != "") {
            flag_bits |= 0x20;
        }
        if (this.contentPacks != null) {
            flag_bits |= 0x40;
        }
        if (this.portals != null) {
            flag_bits |= (byte)128;
        }
        buf.putByte(flag_bits);
        flag_bits = 0;
        if (this.spawns != null) {
            flag_bits = 1;
        }
        if (this.instanceOids != null) {
            flag_bits |= 0x2;
        }
        if (this.instanceSpawns != null) {
            flag_bits |= 0x4;
        }
        if (this.spawnTasks != null) {
            flag_bits |= 0x8;
        }
        buf.putByte(flag_bits);
        buf.putInt(this.id);
        buf.putInt(this.category);
        if (this.name != null && this.name != "") {
            buf.putString(this.name);
        }
        if (this.administrator != null) {
            MarshallingRuntime.marshalObject(buf, (Object)this.administrator);
        }
        buf.putByte((byte)(byte)(this.isPublic ? 1 : 0));
        if (this.password != null && this.password != "") {
            buf.putString(this.password);
        }
        if (this.developers != null) {
            MarshallingRuntime.marshalLinkedList(buf, (Object)this.developers);
        }
        buf.putInt(this.islandType);
        buf.putByte((byte)(byte)(this.createOnStartup ? 1 : 0));
        buf.putInt(this.rating);
        if (this.style != null && this.style != "") {
            buf.putString(this.style);
        }
        if (this.description != null && this.description != "") {
            buf.putString(this.description);
        }
        buf.putInt(this.size);
        buf.putInt(this.populationLimit);
        if (this.contentPacks != null) {
            MarshallingRuntime.marshalLinkedList(buf, (Object)this.contentPacks);
        }
        buf.putByte((byte)(byte)(this.subscriptionActive ? 1 : 0));
        if (this.portals != null) {
            MarshallingRuntime.marshalHashMap(buf, (Object)this.portals);
        }
        if (this.spawns != null) {
            MarshallingRuntime.marshalHashMap(buf, (Object)this.spawns);
        }
        if (this.instanceOids != null) {
            MarshallingRuntime.marshalArrayList(buf, (Object)this.instanceOids);
        }
        if (this.instanceSpawns != null) {
            MarshallingRuntime.marshalHashMap(buf, (Object)this.instanceSpawns);
        }
        if (this.spawnTasks != null) {
            MarshallingRuntime.marshalArrayList(buf, (Object)this.spawnTasks);
        }
    }
    
    public Object unmarshalObject(final AOByteBuffer buf) {
        final byte flag_bits0 = buf.getByte();
        final byte flag_bits2 = buf.getByte();
        this.id = buf.getInt();
        this.category = buf.getInt();
        if ((flag_bits0 & 0x1) != 0x0) {
            this.name = buf.getString();
        }
        if ((flag_bits0 & 0x2) != 0x0) {
            this.administrator = (OID)MarshallingRuntime.unmarshalObject(buf);
        }
        this.isPublic = (buf.getByte() != 0);
        if ((flag_bits0 & 0x4) != 0x0) {
            this.password = buf.getString();
        }
        if ((flag_bits0 & 0x8) != 0x0) {
            this.developers = (LinkedList<OID>)MarshallingRuntime.unmarshalLinkedList(buf);
        }
        this.islandType = buf.getInt();
        this.createOnStartup = (buf.getByte() != 0);
        this.rating = buf.getInt();
        if ((flag_bits0 & 0x10) != 0x0) {
            this.style = buf.getString();
        }
        if ((flag_bits0 & 0x20) != 0x0) {
            this.description = buf.getString();
        }
        this.size = buf.getInt();
        this.populationLimit = buf.getInt();
        if ((flag_bits0 & 0x40) != 0x0) {
            this.contentPacks = (LinkedList<String>)MarshallingRuntime.unmarshalLinkedList(buf);
        }
        this.subscriptionActive = (buf.getByte() != 0);
        if ((flag_bits0 & 0x80) != 0x0) {
            this.portals = (HashMap<String, HashMap<String, Float>>)MarshallingRuntime.unmarshalHashMap(buf);
        }
        if ((flag_bits2 & 0x1) != 0x0) {
            this.spawns = (HashMap<Integer, SpawnData>)MarshallingRuntime.unmarshalHashMap(buf);
        }
        if ((flag_bits2 & 0x2) != 0x0) {
            this.instanceOids = (ArrayList<OID>)MarshallingRuntime.unmarshalArrayList(buf);
        }
        if ((flag_bits2 & 0x4) != 0x0) {
            this.instanceSpawns = (HashMap<OID, ArrayList<SpawnData>>)MarshallingRuntime.unmarshalHashMap(buf);
        }
        if ((flag_bits2 & 0x8) != 0x0) {
            this.spawnTasks = (ArrayList<ScheduledFuture>)MarshallingRuntime.unmarshalArrayList(buf);
        }
        return this;
    }
}

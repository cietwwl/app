// 
// Decompiled by Procyon v0.5.30
// 

package atavism.server.pathing.detour;
/**
 * 
 * @author doter
 * 
 */
public class NavMeshParams {
	public float[] Orig;// ԭ��
	public float TileWidth;// ������
	public float TileHeight;// ����߶�
	public int MaxTiles;// ���������
	public int MaxPolys;// �������

	public NavMeshParams() {
		this.Orig = new float[3];
	}
}

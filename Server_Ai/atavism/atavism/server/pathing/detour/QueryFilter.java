// 
// Decompiled by Procyon v0.5.30
// 

package atavism.server.pathing.detour;
import atavism.server.pathing.recast.Helper;
/**
 * ��ѯ������
 * 
 * @author doter
 * 
 */
public class QueryFilter {
	private float[] _areaCost;// �����ɱ�
	public int IncludeFlags;// ������־
	public int ExcludeFlags;// �ų���־

	public QueryFilter() {
		this._areaCost = new float[NavMeshBuilder.MaxAreas];
		for (int i = 0; i < NavMeshBuilder.MaxAreas; ++i) {
			this._areaCost[i] = 1.0f;
		}
		this.ExcludeFlags = 0;
		this.IncludeFlags = 65535;
	}
	/**
	 * ����
	 * 
	 * @param refId
	 * @param tile
	 * @param poly
	 * @return
	 */
	public Boolean PassFilter(final long refId, final MeshTile tile, final Poly poly) {
		return (poly.Flags & this.IncludeFlags) != 0x0 && (poly.Flags & this.ExcludeFlags) == 0x0;
	}
	/**
	 * ��ȡ�ɱ�
	 * 
	 * @param pax
	 * @param pay
	 * @param paz
	 * @param pbx
	 * @param pby
	 * @param pbz
	 * @param prevRef
	 * @param prevTile
	 * @param prevPoly
	 * @param curRef
	 * @param curTile
	 * @param curPoly
	 * @param nextRef
	 * @param nextTile
	 * @param nextPoly
	 * @return
	 */
	public float GetCost(final float pax, final float pay, final float paz, final float pbx, final float pby, final float pbz, final long prevRef, final MeshTile prevTile, final Poly prevPoly,
			final long curRef, final MeshTile curTile, final Poly curPoly, final long nextRef, final MeshTile nextTile, final Poly nextPoly) {
		return Helper.VDist(pax, pay, paz, pbx, pby, pbz) * this._areaCost[curPoly.getArea()];
	}
	/**
	 * ��ȡ�����ɱ�
	 * 
	 * @param i
	 * @return
	 */
	public float GetAreaCost(final int i) {
		return this._areaCost[i];
	}
	/**
	 * ���õ����ɱ�
	 * 
	 * @param i
	 * @return
	 */
	public void SetAreaCost(final int i, final float cost) {
		this._areaCost[i] = cost;
	}
}

package fr.afcepf.atod.wine.data.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.afcepf.atod.vin.data.exception.WineErrorCode;
import fr.afcepf.atod.vin.data.exception.WineException;
import fr.afcepf.atod.wine.data.api.IDaoGeneric;

@Service
@Transactional
public abstract class DaoGeneric<Obj, ID extends Serializable> implements IDaoGeneric<Obj, ID> {
	@Autowired
	private SessionFactory sf;

	protected Class<? extends Obj> type;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DaoGeneric() {
		 Type t = getClass().getGenericSuperclass();
	     ParameterizedType pt = (ParameterizedType) t;
	     type = (Class) pt.getActualTypeArguments()[0];
	}

	@Override
	public Obj insertObj(Obj o) throws WineException {
		if (o != null) {
			sf.getCurrentSession().save(o);
		} else {
			throw new WineException(WineErrorCode.IMPOSSIBLE_AJOUT_DANS_BASE, "object creation failed");
		}
		return o;
	}

	@Override
	public Boolean updateObj(Obj o) throws WineException {
		Boolean ret = false;
		if (o != null) {
			if (sf.getCurrentSession().merge(o) != null) {
				ret = true;
			}
		} else {
			throw new WineException(WineErrorCode.UPDATE_NON_EFFECTUE_EN_BASE, "object update failed");
		}
		return ret;
	}
	
	@Override
    public Boolean updateWithEvictObj(Obj o) throws WineException {
        Boolean ret = false;
        if (o != null) {
            sf.getCurrentSession().evict(o);
            sf.getCurrentSession().update(o);
                ret = true;
        } else {
            throw new WineException(WineErrorCode.UPDATE_NON_EFFECTUE_EN_BASE, "object update failed");
        }
        return ret;
    }

	@Override
	public Boolean removeObj(Obj o) throws WineException {
		Boolean ret = false;
		if (o != null) {
			sf.getCurrentSession().delete(o);
			ret = true;
		} else {
			throw new WineException(WineErrorCode.IMPOSSIBLE_SUPPRESSION_DANS_BASE, "object removal failed");
		}
		return ret;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Obj findObj(ID id) throws WineException {
		Obj ob = (Obj) new Object();
		ob = (Obj) sf.getCurrentSession().get(type, id);
		if (ob == null) {
			throw new WineException(WineErrorCode.RECHERCHE_NON_PRESENTE_EN_BASE, "object not found");
		}
		return ob;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Obj> findAllObj() throws WineException {
		List<Obj> liste = new ArrayList<Obj>();
		liste = sf.getCurrentSession().createCriteria(type).list();
		if (liste.isEmpty()) {
			throw new WineException(WineErrorCode.RECHERCHE_NON_PRESENTE_EN_BASE, "object not found");
		}
		return liste;
	}

	public SessionFactory getSf() {
		return sf;
	}

	public void setSf(SessionFactory sf) {
		this.sf = sf;
	}

}

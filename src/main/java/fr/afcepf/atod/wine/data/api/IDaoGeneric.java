package fr.afcepf.atod.wine.data.api;

import java.io.Serializable;
import java.util.List;

import fr.afcepf.atod.vin.data.exception.WineException;

public interface IDaoGeneric<Obj,ID extends Serializable> {
	Obj insertObj(Obj o) 		throws WineException;
	Boolean updateObj(Obj o)  	throws WineException;
	Boolean removeObj(Obj o)  	throws WineException;
	Obj findObj(ID id) 			throws WineException;
	List<Obj> findAllObj() 		throws WineException;
}

package io.drift.core.store.serialization;

import io.drift.core.store.ModelStoreException;


public class StorableSerializationException extends ModelStoreException {

	public StorableSerializationException(Exception e) {
		super( "problem with model serialization",e);
	}

}

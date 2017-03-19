
package com.unnsvc.erhena.common.exceptions;

import com.unnsvc.rhena.common.exceptions.RhenaException;

/**
 * The exception thrown by all erhena services
 * 
 * @author noname
 *
 */
public class ErhenaException extends RhenaException {

	private static final long serialVersionUID = 1L;

	public ErhenaException(String message) {

		super(message);
	}

	public ErhenaException(Throwable throwable) {

		super(throwable);
	}

}

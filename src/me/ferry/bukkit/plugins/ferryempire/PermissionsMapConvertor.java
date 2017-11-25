/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferry.bukkit.plugins.ferryempire;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.bukkit.permissions.Permission;

/**
 *
 * @author Fernando
 */
public class PermissionsMapConvertor extends AbstractMap<String, Boolean> {

	private final Collection<Permission> map;

	public PermissionsMapConvertor(Collection<Permission> map) {
		this.map = map;
	}

	@Override
	public Set<Entry<String, Boolean>> entrySet() {
		return new AbstractSet<Entry<String, Boolean>>() {
			@Override
			public Iterator<Entry<String, Boolean>> iterator() {
				return new Iterator<Entry<String, Boolean>>() {
					Iterator<Permission> i = map.iterator();

					@Override
					public boolean hasNext() {
						return i.hasNext();
					}

					@Override
					public Entry<String, Boolean> next() {
						final Permission next = i.next();
						return new Map.Entry<String, Boolean>() {
							@Override
							public String getKey() {
								return next.getName();
							}

							@Override
							public Boolean getValue() {
								return true;
							}

							@Override
							public Boolean setValue(Boolean value) {
								throw new UnsupportedOperationException("Not supported.");
							}
						};
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException("Not supported yet.");
					}
				};
			}

			@Override
			public int size() {
				return map.size();
			}
		};
	}

}

package com.fss.saber.adapter.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class MultiMap<K, V> {

	private final Map<K, List<V>> store;

	public MultiMap() {
		store = new LinkedHashMap<>();
	}

	public MultiMap(final Comparator<K> comparator) {
		store = new TreeMap<>(comparator);
	}
	
	public MultiMap(final boolean sort) {
		if (sort) store = new TreeMap<>();
		else store = new LinkedHashMap<>();
	}

	public final List<V> get(final K key) {
		return store.get(key);
	}

	public final V getFirst(final K key) {
		final List<V> values = store.get(key);
		if (values == null || values.size() == 0) return null;
		return values.get(0);
	}

	public final V getLast(final K key) {
		final List<V> values = store.get(key);
		if (values == null || values.size() == 0) return null;
		return values.get(values.size() - 1);
	}

	public final void put(final K key, final V value) {
		final List<V> values = store.get(key);
		if (values == null) store.put(key, new ArrayList<>(Arrays.asList(value)));
		else if(!values.contains(value)) values.add(value);
	}
	
	public final void putAll(final K key, final Collection<V> value) {
		final List<V> values = store.get(key);
		if (values == null) store.put(key, new ArrayList<>(value));
		else values.addAll(value);
	}
	
	public final void replace(final K key, final V value) {
		final List<V> values = store.get(key);
		if (values == null) store.put(key, new ArrayList<>(Arrays.asList(value)));
		else {
			values.clear();
			values.add(value);
		}
	}
	
	public final void replaceAll(final K key, final Collection<V> value) {
		final List<V> values = store.get(key);
		if (values == null) store.put(key, new ArrayList<>(value));
		else {
			values.clear();
			values.addAll(value);
		}
	}

	public final List<V> remove(final K key) {
		return store.remove(key);
	}
	
	public final boolean remove(final K key, final V value) {
		final List<V> values = store.get(key);
		if (values == null || values.size() == 0) return false;
		else return values.remove(value);
	}
	
	public final V removeFirst(final K key) {
		final List<V> values = store.get(key);
		if (values == null || values.size() == 0) return null;
		else return values.remove(0);
	}

	public final V removeLast(final K key) {
		final List<V> values = store.get(key);
		if (values == null || values.size() == 0) return null;
		else return values.remove(values.size() - 1);
	}
	
	public final void removeAll(final Collection<K> keys) {
		for(K k : keys) store.remove(k);
	}

	
	public final void putAll(final Map<? extends K, ? extends V> m) {
		final Set<? extends K> keys =  m.keySet();
		for(K k : keys) put(k, m.get(k));
	}

	
	public final boolean containsKey(final K key) {
		return store.containsKey(key);
	}
	
	public final boolean containsValue(final String value) {
		return store.values().stream().anyMatch(l -> l.contains(value));
	}
	
	public final int size() {
		return store.size();
	}

	public final boolean isEmpty() {
		return store.isEmpty();
	}

	
	public final void clear() {
		store.clear();
	}

	public final Set<K> keySet() {
		return store.keySet();
	}

	public final Set<V> values() {
		return store.values().stream().flatMap(List::stream).collect(Collectors.toSet());
	}
	
	public final Set<Entry<K, List<V>>> entrySet() {
		return store.entrySet();
	}

	public final void forEach(final BiConsumer<? super K, ? super V> action) {
		Objects.requireNonNull(action);
		for (Entry<K, List<V>> entry : entrySet()) {
			K       k;
			List<V> v;
			try {
				k = entry.getKey();
				v = entry.getValue();
				if (k == null || v == null || v.size() == 0) continue;
			} catch (IllegalStateException ise) {
				throw new ConcurrentModificationException(ise);
			}
			v.forEach(ve -> action.accept(k, ve));
		}
	}

}
